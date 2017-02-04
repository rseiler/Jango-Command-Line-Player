package at.rseiler.jango.sever.http.event;

import at.rseiler.jango.core.song.SongData;
import lombok.Data;

@Data
public class PlayEvent {
    private final SongData songData;
    private final double time;
}
