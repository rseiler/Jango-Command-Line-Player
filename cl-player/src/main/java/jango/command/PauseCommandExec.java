package jango.command;

import at.rseiler.jango.core.command.PauseCommand;
import jango.player.SlavePlayer;

public class PauseCommandExec extends PauseCommand implements CommandExec {
    @Override
    public void execute(SlavePlayer slavePlayer) {
        slavePlayer.pause();
    }
}
