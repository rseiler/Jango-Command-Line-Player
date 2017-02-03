package jango;

import at.rseiler.jango.core.player.Player;
import at.rseiler.jango.core.song.SongData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;


public class TcpClient extends Thread {

    private final Socket socket;
    private Player player = new Player("mplayer");

    public TcpClient(Socket socket) {
        this.socket = socket;
    }


    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            while (true) {
                String[] command = reader.readLine().split("<>");

                switch (command[0]) {
                    case "play":
                        player.play(new SongData(command[1], "", ""), Long.parseLong(command[2]));
                        break;
                    case "pause":
                        player.pause();
                        break;
                    case "ping":
                        break;
                    default:
                        System.out.println("Unknown command: " + Arrays.toString(command));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
