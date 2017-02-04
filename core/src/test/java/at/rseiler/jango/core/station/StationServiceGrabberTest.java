package at.rseiler.jango.core.station;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import org.junit.*;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class StationServiceGrabberTest {
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
    public void topStations() throws Exception {
        List<Station> stations = new StationServiceGrabber("http://localhost:" + port).topStations();
        
        assertThat(stations.size(), is(36));
    }
}
