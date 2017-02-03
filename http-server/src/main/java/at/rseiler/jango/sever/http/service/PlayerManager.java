package at.rseiler.jango.sever.http.service;

import at.rseiler.jango.core.player.Player;
import at.rseiler.jango.sever.http.event.AllClientsDisconnected;
import at.rseiler.jango.sever.http.event.ClientConnectedEvent;
import at.rseiler.jango.sever.http.event.PauseEvent;
import at.rseiler.jango.sever.http.event.PlayEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class PlayerManager {

    private final Player player = new Player("mplayer");
    private final SongService songService;
    private boolean enabled = true;

    @Autowired
    public PlayerManager(SongService songService) {
        this.songService = songService;
    }

    @EventListener
    public void onPlayEvent(PlayEvent event) {
        if (enabled) {
            player.play(event.getSongData());
        }
    }

    @EventListener(PauseEvent.class)
    public void onPauseEvent() {
        player.pause();
    }

    @EventListener(ClientConnectedEvent.class)
    public void onClientConnectedEvent() {
        disable();
    }

    @EventListener(AllClientsDisconnected.class)
    public void onAllClientsDisconnected() {
        enable();
    }

    public void disable() {
        enabled = false;
        player.stop();
    }

    public void enable() {
        enabled = true;

        if (songService.isPlaying()) {
            player.play(songService.getSongData(), songService.getSongTime());
        }
    }
}