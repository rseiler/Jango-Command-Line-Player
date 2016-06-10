package jango;

import at.rseiler.jango.core.Player;
import at.rseiler.jango.core.SongData;
import at.rseiler.jango.core.SongService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class TcpClient extends Thread {

    private final Socket socket;
    private Player player;

    public TcpClient(Socket socket) {
        this.socket = socket;
    }


    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            while (true) {
                String command = reader.readLine();

                if (player != null) {
                    player.stop();
                }

                player = new Player("mplayer", new TcpClient.StaticSongService(new SongData(command, "", "")));
                player.playSongs();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class StaticSongService implements SongService {

        private final SongData songData;

        private StaticSongService(SongData songData) {
            this.songData = songData;
        }

        @Override
        public SongData nextSong() {
            return songData;
        }
    }
}
