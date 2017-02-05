package at.rseiler.jango.clplayer.player;

import at.rseiler.jango.core.player.MPlayer;
import at.rseiler.jango.core.service.decorator.OpDec;
import at.rseiler.jango.core.song.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CancellationException;

public class LocalPlayer implements Player {
    private static final List<OpDec<SongData>> DECORATORS = Arrays.asList(
            new NSSWithConsoleLogging(),
            new NSSWithFileLogging(),
            new NSSWithStoring());
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
        nextSongService = new NSSWithDecorators(new NSSGrabber("http://www.jango.com", stationId), DECORATORS);

        if (!MPlayer.isAlive()) {
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
