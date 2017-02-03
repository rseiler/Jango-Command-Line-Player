package at.rseiler.jango.sever.http.controller;

import at.rseiler.jango.sever.http.event.NextSongEvent;
import at.rseiler.jango.sever.http.event.PauseEvent;
import at.rseiler.jango.sever.http.service.PlayerManager;
import at.rseiler.jango.sever.http.service.SongService;
import at.rseiler.jango.sever.http.service.SongServiceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

@RestController
public class ActionController {

    private final SongServiceManager songServiceManager;
    private final PlayerManager playerManager;
    private final SongService songService;
    private final ApplicationEventPublisher publisher;

    @Autowired
    public ActionController(SongServiceManager songServiceManager, PlayerManager playerManager, SongService songService, ApplicationEventPublisher publisher) {
        this.songServiceManager = songServiceManager;
        this.playerManager = playerManager;
        this.songService = songService;
        this.publisher = publisher;
    }

    @RequestMapping("/station/{stationId:\\d+}")
    public void station(@PathVariable String stationId) throws IOException {
        songServiceManager.setStationId(stationId);

        if (!songService.isPlaying()) {
            publisher.publishEvent(new NextSongEvent());
        }
    }

    @RequestMapping("/output/disable/{id}")
    public void disableOutput(@PathVariable String id) {
        if ("local".equals(id)) {
            playerManager.disable();
        }
    }

    @RequestMapping("/output/enable/{id}")
    public void enableOutput(@PathVariable String id) {
        if ("local".equals(id)) {
            playerManager.enable();
        }
    }

    @RequestMapping("/song/next")
    public void play() {
        publisher.publishEvent(new NextSongEvent());
    }

    @RequestMapping("/song/pause")
    public void pause() {
        publisher.publishEvent(new PauseEvent());
    }

    @ResponseBody
    @RequestMapping("/song/{song:.+}")
    public FileSystemResource song(@PathVariable String song) {
        return new FileSystemResource(new File("songs/" + song));
    }
}
