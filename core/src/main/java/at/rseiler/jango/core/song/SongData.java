package at.rseiler.jango.core.song;


import at.rseiler.jango.core.util.FileUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Holds the data of a song.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SongData {
    private String url;
    private String artist;
    private String song;

    @JsonIgnore
    public String getFileName() {
        return FileUtil.sanitizeName(artist + " - " + song) + ".m4p";
    }

    @JsonIgnore
    public String getArtistSongName() {
        return artist + " - " + song;
    }
}
