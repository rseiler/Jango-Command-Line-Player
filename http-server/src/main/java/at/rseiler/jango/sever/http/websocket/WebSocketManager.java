package at.rseiler.jango.sever.http.websocket;

import at.rseiler.jango.core.command.Command;
import at.rseiler.jango.core.command.PauseCommand;
import at.rseiler.jango.core.command.PlayCommand;
import at.rseiler.jango.core.song.SongData;
import at.rseiler.jango.core.util.ObjectMapperUtil;
import at.rseiler.jango.sever.http.event.PauseEvent;
import at.rseiler.jango.sever.http.event.PlayEvent;
import at.rseiler.jango.sever.http.service.SongService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class WebSocketManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketManager.class);
    private static final double MILLIS_TO_SEC = 1000.0;
    private final List<WebSocketSession> webSocketSessions = new ArrayList<>();
    private final SongService songService;
    private final int port;

    @Autowired
    public WebSocketManager(SongService songService, @Value("${server.port}") int port) {
        this.songService = songService;
        this.port = port;
    }

    @EventListener
    public void onPlaySongEvent(PlayEvent event) {
        broadcast(createPlaySongCmd(event.getSongData(), 0));
    }

    @EventListener(PauseEvent.class)
    public void onPauseEvent() {
        broadcast(new PauseCommand());
    }

    void addSession(WebSocketSession webSocketSession) {
        webSocketSessions.add(webSocketSession);

        if (songService.isPlaying()) {
            try {
                Command command = createPlaySongCmd(songService.getSongData(), songService.getSongTime() / MILLIS_TO_SEC);
                webSocketSession.sendMessage(new TextMessage(ObjectMapperUtil.write(command)));
            } catch (IOException e) {
                LOGGER.error("Failed to send play command", e);
            }
        }
    }

    void removeSession(WebSocketSession session) {
        webSocketSessions.remove(session);
    }

    private void broadcast(Object message) {
        broadcast(ObjectMapperUtil.write(message));
    }

    private void broadcast(byte[] message) {
        webSocketSessions.forEach(webSocketSession -> {
            try {
                webSocketSession.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                LOGGER.error("Failed to broadcast message", e);
            }
        });
    }

    private Command createPlaySongCmd(SongData songData, double time) {
        String url = "/song/" + songData.getFileName();
        return new PlayCommand(new SongData(url, songData.getArtist(), songData.getSong()), time);
    }
}
