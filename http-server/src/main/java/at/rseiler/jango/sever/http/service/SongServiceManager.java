package at.rseiler.jango.sever.http.service;

import at.rseiler.jango.core.RequestService;
import at.rseiler.jango.core.song.*;
import at.rseiler.jango.sever.http.event.NextSongEvent;
import at.rseiler.jango.sever.http.event.PlayEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class SongServiceManager {

    private static final List<Class<? extends NextSongServiceDecorator>> SONG_SERVICE_DECORATORS = Arrays.asList(
            NextSongServiceWithConsoleLogging.class,
            NextSongServiceWithStoring.class
    );
    private final RequestService requestService;
    private final ApplicationEventPublisher publisher;
    private NextSongService nextSongService;

    @Autowired
    public SongServiceManager(RequestService requestService, ApplicationEventPublisher publisher) {
        this.requestService = requestService;
        this.publisher = publisher;
    }

    @EventListener(NextSongEvent.class)
    public void handleNextSong() {
        if (nextSongService != null) {
            publisher.publishEvent(new PlayEvent(nextSongService.getNextSong()));
        }
    }

    public void setStationId(String stationId) throws IOException {
        nextSongService = new SongServiceBuilder(new NextSongServiceImpl(requestService, stationId))
                .withDecorators(SONG_SERVICE_DECORATORS)
                .build();
    }

}
