package at.rseiler.jango.command;

import at.rseiler.jango.core.song.SongData;
import at.rseiler.jango.clplayer.command.PauseCommandExec;
import at.rseiler.jango.clplayer.command.PlayCommandExec;
import at.rseiler.jango.clplayer.player.SlavePlayer;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CommandExecTest {
    private SlavePlayer slavePlayer;

    @Before
    public void setUp() throws Exception {
        slavePlayer = mock(SlavePlayer.class);
    }

    @Test
    public void pause() throws Exception {
        new PauseCommandExec().execute(slavePlayer);

        verify(slavePlayer).pause();
    }

    @Test
    public void play() throws Exception {
        new PlayCommandExec(new SongData("http://localhost/song/song.mp4", "", ""), 1).execute(slavePlayer);

        verify(slavePlayer).play("http://localhost/song/song.mp4", 1.0);
    }
}