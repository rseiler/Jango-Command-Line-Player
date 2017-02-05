package at.rseiler.jango.core.song;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class NSSGrabberTest {
    @ClassRule
    public static WireMockClassRule WIRE_MOCK_RULE = new WireMockClassRule(WireMockConfiguration.options().dynamicPort().usingFilesUnderClasspath("wiremock"));

    @Rule
    public WireMockClassRule wireMockRule = WIRE_MOCK_RULE;

    private int port;

    @Before
    public void setUp() throws Exception {
        port = wireMockRule.port();
    }

    @Test
    public void getNextSong() throws Exception {
        NSSGrabber nextSongService = new NSSGrabber("http://localhost:" + port, "263448188");

        assertThat(nextSongService.getNextSong(), is(new SongData("http://cd09.64.aac.jango.com/89/45/98/894598439861363240.m4a", null, null)));
    }
}