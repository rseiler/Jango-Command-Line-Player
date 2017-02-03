package at.rseiler.jango.core.player;

import at.rseiler.jango.core.song.SongData;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public class Player {

    private final String pathToMPlayer;
    private Process mPlayerProcess;
    private CompletableFuture<SongData> future;

    public Player(String pathToMPlayer) {
        this.pathToMPlayer = pathToMPlayer;
    }

    public CompletableFuture<SongData> play(SongData songData) {
        return play(songData, 0);
    }

    /**
     * Start to play the songs from the station.
     */
    public CompletableFuture<SongData> play(SongData songData, long songTime) {
        if (future != null && isAlive()) {
            future.cancel(true);
            stop();
        }

        future = CompletableFuture.supplyAsync(() -> {
            double songTimeInSec = songTime / 1000;

            try {
                mPlayerProcess = new ProcessBuilder(pathToMPlayer, "-really-quiet", songData.getUrl(), "-ss", Double.toString(songTimeInSec)).start();
                mPlayerProcess.waitFor();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
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
                e.printStackTrace();
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
                e.printStackTrace();
                mPlayerProcess.destroy();
            }
        }
    }

    private boolean isAlive() {
        return mPlayerProcess != null && mPlayerProcess.isAlive();
    }
}
