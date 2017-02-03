package at.rseiler.jango.sever.http.tcp;

import at.rseiler.jango.core.song.SongData;
import at.rseiler.jango.sever.http.util.IpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Optional;

class TcpConnectionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(TcpConnectionHandler.class);
    private final Socket socket;
    private final DataOutputStream output;
    private final BufferedReader input;
    private final int port;

    TcpConnectionHandler(Socket socket, int port) throws IOException {
        this.socket = socket;
        this.output = new DataOutputStream(socket.getOutputStream());
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.port = port;
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
            output.writeBytes("play<>http://" + IpUtil.getLocalIp() + ":" + port + "/song/" + songData.getFileName() + "<>" + songTime + "\n");
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void sendPause() throws RuntimeException {
        try {
            output.writeBytes("pause\n");
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
