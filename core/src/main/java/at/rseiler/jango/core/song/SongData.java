package at.rseiler.jango.core.song;


import at.rseiler.jango.core.util.FileUtil;
import lombok.Data;

/**
 * Holds the data of a song.
 */
@Data
public class SongData {
    private final String url;
    private final String artist;
    private final String song;

    public String getFileName() {
        return FileUtil.sanitizeName(artist + " - " + song) + ".m4p";
    }

    public String getArtistSongName() {
        return artist + " - " + song;
    }
}
