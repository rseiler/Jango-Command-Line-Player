package at.rseiler.jango.core.util;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class HttpUtilTest {
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
    public void grabData() throws Exception {
        String response = HttpUtil.grabData("http://localhost:" + port + "/wien/");

        verify(getRequestedFor(urlEqualTo("/wien/")));
        assertThat(response.length(), is(28708));
    }

    @Test
    public void prepareConnection() throws Exception {
        HttpUtil.grabData("http://localhost:" + port + "/wien/");

        verify(getRequestedFor(urlEqualTo("/wien/")));
    }
}
