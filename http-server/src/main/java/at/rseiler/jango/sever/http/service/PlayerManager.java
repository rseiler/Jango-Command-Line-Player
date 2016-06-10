package at.rseiler.jango.sever.http.service;

import at.rseiler.jango.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PlayerManager {

    private final RequestService requestService;
    private Player player;

    @Autowired
    public PlayerManager(RequestService requestService) {
        this.requestService = requestService;
    }

    public void play(String stationId) throws IOException {
        if (player != null) {
            player.stop();
        }

        final SongService songService = new SongServiceWithStoring(
                new SongServiceImpl(requestService, "http://www.jango.com/stations/" + stationId + "/tunein")
        );
        player = new Player("mplayer", songService);
        player.playSongs();
    }
}
