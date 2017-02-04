package at.rseiler.jango.clplayer;

import at.rseiler.jango.core.ObjectMapperUtil;
import at.rseiler.jango.core.command.Command;
import at.rseiler.jango.core.service.ExecuteService;
import fr.xebia.extras.selma.Selma;
import at.rseiler.jango.clplayer.command.mapper.CommandExecMapper;
import at.rseiler.jango.clplayer.player.SlavePlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class TcpClient extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(TcpClient.class);
    private final ExecuteService executeService = new ExecuteService(Selma.builder(CommandExecMapper.class).build());
    private final SlavePlayer slavePlayer;
    private final Socket socket;
    private final DataOutputStream dataOutputStream;

    public TcpClient(SlavePlayer slavePlayer, Socket socket) throws IOException {
        this.slavePlayer = slavePlayer;
        this.socket = socket;
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            while (true) {
                String line = reader.readLine();

                if (!line.equals("ping")) {
                    Command command = ObjectMapperUtil.read(line, Command.class);
                    executeService.execute(command, slavePlayer);
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error reading socket", e);
        }
    }

    public void write(byte[] command) {
        try {
            dataOutputStream.write(command);
            dataOutputStream.writeBytes("\n");
            dataOutputStream.flush();
        } catch (IOException e) {
            LOGGER.error("Failed to write command to output stream", e);
        }
    }
}
