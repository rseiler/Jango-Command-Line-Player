package at.rseiler.jango.clplayer.command;

import at.rseiler.jango.clplayer.player.SlavePlayer;

public interface CommandExec {
    void execute(SlavePlayer slavePlayer);
}
