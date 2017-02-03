package at.rseiler.jango.sever.http.tcp;

import at.rseiler.jango.sever.http.event.AllClientsDisconnected;
import at.rseiler.jango.sever.http.event.ClientConnectedEvent;
import at.rseiler.jango.sever.http.event.PauseEvent;
import at.rseiler.jango.sever.http.event.PlayEvent;
import at.rseiler.jango.sever.http.service.SongService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class TcpServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(TcpServer.class);
    private final List<TcpConnectionHandler> handlers = new ArrayList<>();
    private final ApplicationEventPublisher publisher;
    private final SongService songService;
    private final int port;
    private final int tcpPort;

    @Autowired
    public TcpServer(SongService songService,
                     ApplicationEventPublisher publisher,
                     @Value("${server.port}") int port,
                     @Value("${server.tcp.port}") int tcpPort) {
        this.songService = songService;
        this.publisher = publisher;
        this.port = port;
        this.tcpPort = tcpPort;
    }

    @PostConstruct
    public void init() throws IOException {
        Thread thread = new ConnectionListener(tcpPort);
        thread.start();
    }

    @EventListener
    public synchronized void onPlayEvent(PlayEvent event) {
        onEvent(handler -> handler.sendPlaySong(event.getSongData(), 0));
    }

    @EventListener(PauseEvent.class)
    public synchronized void onPauseEvent() {
        onEvent(TcpConnectionHandler::sendPause);
    }

    @Scheduled(fixedRate = 10000)
    public synchronized void ping() {
        onEvent(TcpConnectionHandler::ping);
    }

    private void onEvent(HandlerCallable handlerCallable) {
        for (Iterator<TcpConnectionHandler> iterator = handlers.iterator(); iterator.hasNext(); ) {
            TcpConnectionHandler handler = iterator.next();

            try {
                handlerCallable.apply(handler);
            } catch (RuntimeException e) {
                iterator.remove();
                removeHandler(handler);
            }
        }
    }

    private void removeHandler(TcpConnectionHandler handler) {
        handler.close();

        if (handlers.isEmpty()) {
            publisher.publishEvent(new AllClientsDisconnected());
        }
    }

    @FunctionalInterface
    private interface HandlerCallable {
        void apply(TcpConnectionHandler handler);
    }

    private final class ConnectionListener extends Thread {
        private final ServerSocket serverSocket;

        private ConnectionListener(int port) throws IOException {
            this.serverSocket = new ServerSocket(port);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    newTcpConnection(new TcpConnectionHandler(serverSocket.accept(), port));
                } catch (IOException e) {
                    LOGGER.error("Failed to create TcpConnectionHandler", e);
                }
            }
        }

        private void newTcpConnection(TcpConnectionHandler handler) {
            publisher.publishEvent(new ClientConnectedEvent());
            handlers.add(handler);

            if (songService.isPlaying()) {
                try {
                    handler.sendPlaySong(songService.getSongData(), songService.getSongTime());
                } catch (RuntimeException e) {
                    removeHandler(handler);
                }
            }
        }
    }
}
