package at.rseiler.jango.sever.http.websocket;

import at.rseiler.jango.core.song.SongData;
import at.rseiler.jango.sever.http.command.Command;
import at.rseiler.jango.sever.http.command.PauseCmd;
import at.rseiler.jango.sever.http.command.PlayCmd;
import at.rseiler.jango.sever.http.event.PauseEvent;
import at.rseiler.jango.sever.http.event.PlayEvent;
import at.rseiler.jango.sever.http.service.SongService;
import at.rseiler.jango.sever.http.util.IpUtil;
import com.google.gson.Gson;
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
    private static final Gson GSON = new Gson();
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
        broadcast(new Command(new PauseCmd()));
    }

    void addSession(WebSocketSession webSocketSession) {
        webSocketSessions.add(webSocketSession);

        if (songService.isPlaying()) {
            try {
                Command command = createPlaySongCmd(songService.getSongData(), songService.getSongTime() / MILLIS_TO_SEC);
                webSocketSession.sendMessage(new TextMessage(GSON.toJson(command)));
            } catch (IOException e) {
                LOGGER.error("Failed to send play command", e);
            }
        }
    }

    void removeSession(WebSocketSession session) {
        webSocketSessions.remove(session);
    }

    private void broadcast(Object message) {
        broadcast(GSON.toJson(message));
    }

    private void broadcast(String message) {
        webSocketSessions.forEach(webSocketSession -> {
            try {
                webSocketSession.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                LOGGER.error("Failed to broadcast message", e);
            }
        });
    }

    private Command createPlaySongCmd(SongData songData, double time) {
        String url = "http://" + IpUtil.getLocalIp() + ":" + port + "/song/" + songData.getFileName();
        return new Command(new PlayCmd(new SongData(url, songData.getArtist(), songData.getSong()), time));
    }
}
