package at.rseiler.jango.sever.http.tcp;

import at.rseiler.jango.core.RequestService;
import at.rseiler.jango.core.SongService;
import at.rseiler.jango.core.SongServiceImpl;
import at.rseiler.jango.core.SongServiceWithStoring;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.ServerSocket;

@Service
public class TcpServer {

    private final ServerSocket serverSocket;
    private final SongService songService;

    @Autowired
    public TcpServer(RequestService requestService) throws IOException {
        serverSocket = new ServerSocket(9888);
        songService = new SongServiceWithStoring(
                new SongServiceImpl(requestService, "http://www.jango.com/stations/" + 373752453 + "/tunein")
        );

        Thread thread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        new TcpConnectionHandler(serverSocket.accept(), songService).start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }

}
