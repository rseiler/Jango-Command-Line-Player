package at.rseiler.jango.clplayer.player;

import at.rseiler.jango.core.player.MPlayer;
import at.rseiler.jango.core.song.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CancellationException;

public class LocalPlayer implements Player {
    private static final List<Class<? extends NSSDecorator>> DECORATORS = Arrays.asList(
            NSSWithConsoleLogging.class,
            NSSWithFileLogging.class,
            NSSWithStoring.class
    );
    private final MPlayer MPlayer = new MPlayer();
    private NextSongService nextSongService;

    @Override
    public void onPause() {
        pause();
    }

    @Override
    public void onNext() {
        stop();
    }

    @Override
    public void onStation(String stationId) throws IOException {
        setStationId(stationId);
    }

    @Override
    public void pause() {
        MPlayer.pause();
    }

    @Override
    public void stop() {
        MPlayer.stop();
    }

    public void setStationId(String stationId) throws IOException {
        nextSongService = new SongServiceBuilder(new NextSongServiceImpl("http://www.jango.com", stationId))
                .withDecorators(DECORATORS)
                .build();

        if(!MPlayer.isAlive()) {
            playNextSong();
        }
    }

    private void playNextSong() {
        MPlayer.play(nextSongService.getNextSong())
                .whenComplete(this::whenComplete);
    }

    private void whenComplete(SongData songData, Throwable throwable) {
        if (!(throwable instanceof CancellationException)) {
            playNextSong();
        }
    }
}
