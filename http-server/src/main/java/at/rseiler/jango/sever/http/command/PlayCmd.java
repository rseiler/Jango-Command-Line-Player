package at.rseiler.jango.sever.http.command;

import at.rseiler.jango.core.song.SongData;

public class PlayCmd {

    private static final String ID = "play";
    private final SongData songData;
    private final double time;

    public PlayCmd(SongData songData, double time) {
        this.songData = songData;
        this.time = time;
    }

    public SongData getSongData() {
        return songData;
    }

    public double getTime() {
        return time;
    }

    public String getId() {
        return ID;
    }
}
