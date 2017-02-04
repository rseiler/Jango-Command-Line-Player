package at.rseiler.jango.sever.http.command;

import at.rseiler.jango.core.song.SongData;
import at.rseiler.jango.sever.http.event.NextSongEvent;
import at.rseiler.jango.sever.http.event.PauseEvent;
import at.rseiler.jango.sever.http.event.PlayEvent;
import at.rseiler.jango.sever.http.event.StationEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationEventPublisher;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CommandExecTest {
    private static final String JSON_NEXT = "{\"command\":\"next\"}";
    private static final String JSON_STATION = "{\"command\":\"station\",\"stationId\":\"123\"}";
    private static final String JSON_PAUSE = "{\"command\":\"pause\"}";
    private static final String JSON_PLAY = "{\"command\":\"play\",\"songData\":{\"url\":\"http://localhost/song/song.mp4\",\"artist\":null,\"song\":null},\"time\":1.0}";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private ApplicationEventPublisher publisher;

    @Before
    public void setUp() throws Exception {
        publisher = mock(ApplicationEventPublisher.class);
    }

    @Test
    public void nextSong() throws Exception {
        OBJECT_MAPPER.readValue(JSON_NEXT, CommandExec.class).execute(publisher);

        verify(publisher).publishEvent(new NextSongEvent());
    }

    @Test
    public void pause() throws Exception {
        OBJECT_MAPPER.readValue(JSON_PAUSE, CommandExec.class).execute(publisher);

        verify(publisher).publishEvent(new PauseEvent());
    }

    @Test
    public void play() throws Exception {
        OBJECT_MAPPER.readValue(JSON_PLAY, CommandExec.class).execute(publisher);

        verify(publisher).publishEvent(new PlayEvent(new SongData("http://localhost/song/song.mp4", null, null), 1.0));
    }

    @Test
    public void station() throws Exception {
        OBJECT_MAPPER.readValue(JSON_STATION, CommandExec.class).execute(publisher);

        verify(publisher).publishEvent(new StationEvent("123"));
    }
}
