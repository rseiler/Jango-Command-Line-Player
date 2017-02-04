package at.rseiler.jango.sever.http.command;

import at.rseiler.jango.core.song.SongData;
import at.rseiler.jango.sever.http.event.NextSongEvent;
import at.rseiler.jango.sever.http.event.PauseEvent;
import at.rseiler.jango.sever.http.event.PlayEvent;
import at.rseiler.jango.sever.http.event.StationEvent;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationEventPublisher;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CommandExecTest {
    private ApplicationEventPublisher publisher;

    @Before
    public void setUp() throws Exception {
        publisher = mock(ApplicationEventPublisher.class);
    }

    @Test
    public void nextSong() throws Exception {
        new NextSongCommandExec().execute(publisher);

        verify(publisher).publishEvent(new NextSongEvent());
    }

    @Test
    public void pause() throws Exception {
        new PauseCommandExec().execute(publisher);

        verify(publisher).publishEvent(new PauseEvent());
    }

    @Test
    public void play() throws Exception {
        new PlayCommandExec(createSongData(), 1).execute(publisher);

        verify(publisher).publishEvent(new PlayEvent(createSongData(), 1.0));
    }

    @Test
    public void station() throws Exception {
        new StationCommandExec("123").execute(publisher);

        verify(publisher).publishEvent(new StationEvent("123"));
    }

    private SongData createSongData() {
        return new SongData("http://localhost/song/song.mp4", "artist", "song");
    }
}
