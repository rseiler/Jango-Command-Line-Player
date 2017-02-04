package at.rseiler.jango.clplayer.command;

import at.rseiler.jango.core.util.SongUtil;
import at.rseiler.jango.core.command.PlayCommand;
import at.rseiler.jango.core.song.SongData;
import at.rseiler.jango.clplayer.player.SlavePlayer;

public class PlayCommandExec extends PlayCommand implements CommandExec {
    public PlayCommandExec() {
    }

    public PlayCommandExec(SongData songData, double time) {
        super(songData, time);
    }

    @Override
    public void execute(SlavePlayer slavePlayer) {
        slavePlayer.play(getSongData().getUrl(), getTime());
        System.out.println(SongUtil.getDateTimeArtistSong(getSongData()));
    }
}
