package at.rseiler.jango.core;

import java.io.IOException;
import java.io.OutputStream;

public class Player {

    private Process mplayerProcess;
    private long startTime;
    private boolean running = true;

    private final String pathToMplayer;
    private final SongService songService;

    public Player(String pathToMplayer, SongService songService) {
        this.pathToMplayer = pathToMplayer;
        this.songService = songService;
    }

    /**
     * Start to play the songs from the station.
     */
    public void playSongs() {
        Thread player = new Thread() {
            @Override
            public void run() {
                while (running) {
                    try {
                        startTime = System.currentTimeMillis();
                        SongData songData = songService.nextSong();
                        mplayerProcess = new ProcessBuilder(pathToMplayer, "-really-quiet", songData.getUrl()).start();
                        mplayerProcess.waitFor();
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }


        };
        player.setDaemon(true);
        player.start();
    }

    public double playTimeInSec() {
        return (System.currentTimeMillis() - startTime) / 100.0;
    }

    public void pause() {
        try {
            OutputStream os = mplayerProcess.getOutputStream();
            os.write("p".getBytes());
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        running = false;
        killPlayer();
    }

    /**
     * Quites the mplayer.
     */
    public void killPlayer() {
        if (mplayerProcess != null) {
            OutputStream os = mplayerProcess.getOutputStream();
            try {
                os.write("q".getBytes());
                os.flush();
            } catch (IOException e) {
                mplayerProcess.destroy();
            }
        }
    }
}
