package at.rseiler.jango.core.song;

import at.rseiler.jango.core.fortest.SongDataUtil;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;

import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;

public class NextSongServiceWithConsoleLoggingTest {
    private static final Pattern DATETIME_ARTIST_SONG_PATTERN = Pattern.compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2} \\| artist - song");

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @Test
    public void getNextSong() throws Exception {
        new NSSWithConsoleLogging().exec(SongDataUtil.createSongData());

        assertTrue(DATETIME_ARTIST_SONG_PATTERN.matcher(systemOutRule.getLog()).find());
    }
}
