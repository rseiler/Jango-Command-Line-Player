package at.rseiler.jango.clplayer.command;

import at.rseiler.jango.core.command.PauseCommand;
import at.rseiler.jango.clplayer.player.SlavePlayer;

public class PauseCommandExec extends PauseCommand implements CommandExec {
    @Override
    public void execute(SlavePlayer slavePlayer) {
        slavePlayer.pause();
    }
}
