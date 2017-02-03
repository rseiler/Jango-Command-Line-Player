package jango;

import jango.player.SlavePlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;


public class TcpClient extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(TcpClient.class);
    private final SlavePlayer slavePlayer;
    private final Socket socket;

    public TcpClient(SlavePlayer slavePlayer, Socket socket) {
        this.slavePlayer = slavePlayer;
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            while (true) {
                String line = reader.readLine();
                String[] command = line.split("<>");

                switch (command[0]) {
                    case "play":
                        slavePlayer.play(command[1], Long.parseLong(command[2]));
                        break;
                    case "pause":
                        slavePlayer.pause();
                        break;
                    case "ping":
                        break;
                    default:
                        System.out.println("Unknown command: " + Arrays.toString(command));
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error reading socket", e);
        }
    }

    public void write(String command) {
        try {
            new DataOutputStream(socket.getOutputStream()).writeBytes(command + "\n");
        } catch (IOException e) {
            LOGGER.error("Failed to write command to output stream", e);
        }
    }
}
