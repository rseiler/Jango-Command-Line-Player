package at.rseiler.jango.sever.http.service;

import at.rseiler.jango.core.service.decorator.OpDec;
import at.rseiler.jango.core.song.*;
import at.rseiler.jango.sever.http.event.NextSongEvent;
import at.rseiler.jango.sever.http.event.PlayEvent;
import at.rseiler.jango.sever.http.event.StationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class SongServiceManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(SongServiceManager.class);
    private static final List<OpDec<SongData>> DECORATORS = Arrays.asList(
            new NSSWithConsoleLogging(),
            new NSSWithFileLogging(),
            new NSSWithStoring()
    );
    private final ApplicationEventPublisher publisher;
    private NextSongService nextSongService;

    @Autowired
    public SongServiceManager(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @EventListener(StationEvent.class)
    public void handleStationEvent(StationEvent stationEvent) {
        try {
            nextSongService = new NSSWithDecorators(new NSSGrabber("http://www.jango.com", stationEvent.getStationId()), DECORATORS);
        } catch (IOException e) {
            LOGGER.error("Failed to create NextSongService", e);
        }
    }

    @EventListener(NextSongEvent.class)
    public void handleNextSongEvent() {
        if (nextSongService != null) {
            publisher.publishEvent(new PlayEvent(nextSongService.getNextSong(), 0.0));
        }
    }

}
