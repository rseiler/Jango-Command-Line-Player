package at.rseiler.jango.sever.http.event;

import at.rseiler.jango.core.song.SongData;

public class PlayEvent {
    private final SongData songData;

    public PlayEvent(SongData songData) {
        this.songData = songData;
    }

    public SongData getSongData() {
        return songData;
    }

    @Override
    public String toString() {
        return "PlayEvent{" +
                "songData=" + songData +
                '}';
    }
}
