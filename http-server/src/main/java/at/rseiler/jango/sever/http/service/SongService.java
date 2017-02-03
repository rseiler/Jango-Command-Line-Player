package at.rseiler.jango.sever.http.service;

import at.rseiler.jango.core.song.SongData;
import at.rseiler.jango.sever.http.event.NextSongEvent;
import at.rseiler.jango.sever.http.event.PauseEvent;
import at.rseiler.jango.sever.http.event.PlayEvent;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SongService {

    private static final Pattern LENGTH_PATTERN = Pattern.compile("ID_LENGTH=(\\d+.\\d+)");
    private final ApplicationEventPublisher publisher;
    private Info info = new Info(null, 0);
    private Timer timer;
    private SongData songData;

    @Autowired
    public SongService(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @EventListener
    void onPlaySongEvent(PlayEvent event) throws IOException {
        songData = event.getSongData();
        long length = getSongLength(songData);
        info = new Info(System.currentTimeMillis(), length);
        createTimer();
    }

    @EventListener(PauseEvent.class)
    void onPauseEvent() throws IOException {
        if (timer == null) {
            createTimer();
        } else {
            timer.cancel();
            timer = null;
        }

        info.pause();
    }

    public boolean isPlaying() {
        return info.isPlaying();
    }

    public SongData getSongData() {
        return songData;
    }

    public long getSongTime() {
        return info.getTime();
    }

    private long getSongLength(SongData songData) throws IOException {
        Process process = new ProcessBuilder("mplayer", "-really-quiet", "-vo", "null", "-ao", "null", "-frames", "0", "-identify", songData.getUrl()).start();
        Matcher matcher = LENGTH_PATTERN.matcher(IOUtils.toString(process.getInputStream(), StandardCharsets.UTF_8));

        if (matcher.find()) {
            return (long) (Double.parseDouble(matcher.group(1)) * 1000);
        }

        return -1;
    }

    private void createTimer() {
        if (timer != null) {
            timer.cancel();
        }

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                publisher.publishEvent(new NextSongEvent());
            }
        }, info.getRemainingTime());
    }

    private static class Info {
        private Long startTime;
        private long duration = 0;
        private final long length;

        private Info(Long startTime, long length) {
            this.startTime = startTime;
            this.length = length;
        }

        private boolean isPlaying() {
            return startTime != null;
        }

        private void pause() {
            if (startTime == null) {
                startTime = System.currentTimeMillis();
            } else {
                duration += System.currentTimeMillis() - startTime;
                startTime = null;
            }
        }

        private long getTime() {
            return (startTime != null ? System.currentTimeMillis() - startTime : 0) + duration;
        }

        private long getRemainingTime() {
            long time = length - getTime();
            return time > 0 ? time : 0;
        }
    }
}
