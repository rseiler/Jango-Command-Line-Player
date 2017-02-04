package at.rseiler.jango.core.player;

import at.rseiler.jango.core.song.SongData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public class MPlayer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MPlayer.class);
    private Process mPlayerProcess;
    private CompletableFuture<SongData> future;

    public CompletableFuture<SongData> play(SongData songData) {
        return play(songData, 0);
    }

    /**
     * Start to play the songs from the station.
     */
    public CompletableFuture<SongData> play(SongData songData, double songTime) {
        if (future != null && isAlive()) {
            future.cancel(true);
            stop();
        }

        future = CompletableFuture.supplyAsync(() -> {
            double songTimeInSec = songTime / 1000;

            try {
                mPlayerProcess = new ProcessBuilder("mplayer", "-really-quiet", songData.getUrl(), "-ss", Double.toString(songTimeInSec)).start();
                mPlayerProcess.waitFor();
            } catch (IOException | InterruptedException e) {
                LOGGER.error("Failed to create mplayer process", e);
            }

            return songData;
        });

        return future;
    }

    public void pause() {
        if (isAlive()) {
            try {
                OutputStream os = mPlayerProcess.getOutputStream();
                os.write("p".getBytes(StandardCharsets.UTF_8));
                os.flush();
            } catch (IOException e) {
                LOGGER.error("Failed to pause mplayer", e);
            }
        }
    }

    public void stop() {
        if (isAlive()) {
            OutputStream os = mPlayerProcess.getOutputStream();

            try {
                os.write("q".getBytes(StandardCharsets.UTF_8));
                os.flush();
                os.close();
            } catch (IOException e) {
                LOGGER.error("Failed to quite mplayer. Trying to destroy process.", e);
                mPlayerProcess.destroy();
            }
        }
    }

    public boolean isAlive() {
        return mPlayerProcess != null && mPlayerProcess.isAlive();
    }
}
