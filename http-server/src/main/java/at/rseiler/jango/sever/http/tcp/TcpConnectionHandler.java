package at.rseiler.jango.sever.http.tcp;

import at.rseiler.jango.core.command.PauseCommand;
import at.rseiler.jango.core.command.PlayCommand;
import at.rseiler.jango.core.song.SongData;
import at.rseiler.jango.core.util.ObjectMapperUtil;
import at.rseiler.jango.sever.http.util.IpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

class TcpConnectionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(TcpConnectionHandler.class);
    private final Socket socket;
    private final int port;
    private DataOutputStream output;
    private BufferedReader input;

    TcpConnectionHandler(Socket socket, int port) throws IOException {
        this.socket = socket;
        this.port = port;
    }

    TcpConnectionHandler init() throws IOException {
        this.output = new DataOutputStream(socket.getOutputStream());
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        return this;
    }

    Optional<String> readLine() {
        try {
            if (input.ready()) {
                return Optional.ofNullable(input.readLine());
            }
        } catch (IOException e) {
            LOGGER.warn("Failed to read from input stream");
        }

        return Optional.empty();
    }

    void sendPlaySong(SongData songData, long songTime) throws RuntimeException {
        try {
            String url = "http://" + IpUtil.getLocalIp() + ":" + port + "/song/" + songData.getFileName();
            SongData remoteSongData = new SongData(url, songData.getArtist(), songData.getSong());
            PlayCommand playCommand = new PlayCommand(remoteSongData, songTime);
            output.write(ObjectMapperUtil.write(playCommand));
            output.writeBytes("\n");
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void sendPause() throws RuntimeException {
        try {
            output.write(ObjectMapperUtil.write(new PauseCommand()));
            output.writeBytes("\n");
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void ping() throws RuntimeException {
        try {
            output.writeBytes("ping\n");
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void close() {
        try {
            output.close();
            socket.close();
        } catch (Exception e) {
            LOGGER.error("Failed to close TCP connection", e);
        }
    }
}
