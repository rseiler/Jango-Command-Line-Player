package at.rseiler.jango.core.util;

import at.rseiler.jango.core.song.SongData;
import org.junit.Test;

import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;

public class SongUtilTest {
    static final Pattern DATETIME_ARTIST_SONG_PATTERN = Pattern.compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2} \\| artist - song");

    @Test
    public void getDateTimeArtistSong() throws Exception {
        String dateTimeArtistSong = SongUtil.getDateTimeArtistSong(new SongData("http://localhost/song/song.m4p", "artist", "song"));

        assertTrue(DATETIME_ARTIST_SONG_PATTERN.matcher(dateTimeArtistSong).find());
    }
}
