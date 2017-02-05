package at.rseiler.jango.core.song;

import at.rseiler.jango.core.fortest.SongDataUtil;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;

public class NSSWithFileLoggingTest {
    private static final Pattern DATETIME_ARTIST_SONG_PATTERN = Pattern.compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2} \\| artist - song");
    private File file;

    @Before
    public void setUp() throws Exception {
        file = new File("songlist.txt");
        file.delete();
    }

    @After
    public void tearDown() throws Exception {
        file.delete();
    }

    @Test
    public void getNextSong() throws Exception {
        new NSSWithFileLogging().exec(SongDataUtil.createSongData());

        assertTrue(DATETIME_ARTIST_SONG_PATTERN.matcher(FileUtils.readFileToString(file, StandardCharsets.UTF_8)).find());
    }
}
