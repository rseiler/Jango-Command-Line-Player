package at.rseiler.jango.core.song;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import org.apache.commons.io.FileUtils;
import org.junit.*;

import java.io.File;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class NSSWithStoringTest {
    @ClassRule
    public static WireMockClassRule WIRE_MOCK_RULE = new WireMockClassRule(WireMockConfiguration.options().dynamicPort().usingFilesUnderClasspath("wiremock"));

    @Rule
    public WireMockClassRule wireMockRule = WIRE_MOCK_RULE;

    private int port;
    private File file;

    @Before
    public void setUp() throws Exception {
        port = wireMockRule.port();
        file = new File("songs" + File.separator + "artist - song.m4p");
        file.delete();
    }

    @After
    public void tearDown() throws Exception {
        file.delete();
    }

    @Test
    public void getNextSong() throws Exception {
        new NSSWithStoring(new ForTestNextSongService(port)).getNextSong();

        assertThat(FileUtils.readFileToString(file, StandardCharsets.UTF_8), is("song.m4p"));
    }
}
