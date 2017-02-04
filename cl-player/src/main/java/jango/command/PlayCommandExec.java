package jango.command;

import at.rseiler.jango.core.SongUtil;
import at.rseiler.jango.core.command.PlayCommand;
import jango.player.SlavePlayer;

public class PlayCommandExec extends PlayCommand implements CommandExec {
    @Override
    public void execute(SlavePlayer slavePlayer) {
        slavePlayer.play(getSongData().getUrl(), getTime());
        System.out.println(SongUtil.getDateTimeArtingSong(getSongData()));
    }
}
