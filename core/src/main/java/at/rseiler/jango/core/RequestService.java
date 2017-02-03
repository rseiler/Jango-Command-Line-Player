package at.rseiler.jango.core;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class RequestService {

    private final HttpClient httpClient = HttpClientBuilder.create().build();

    /**
     * Fetches the data from the given url.
     *
     * @param url the url which will be opened
     * @return the content
     */
    public String grabData(String url) {
        String result = "";

        try {
            HttpGet method = new HttpGet(url);
            method.addHeader("X-Requested-With", "XMLHttpRequest");
            HttpResponse httpResponse = httpClient.execute(method);
            result = IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8").replaceAll("\\n", "");
            EntityUtils.consume(httpResponse.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Opens the main page to get the necessary cookies.
     */
    public void prepareConnection(String uri) throws IOException {
        EntityUtils.consume(httpClient.execute(new HttpGet(uri)).getEntity());
//        EntityUtils.consume(httpClient.execute(new HttpGet("http://www.jango.com/stations/" + stationId + "/tunein")).getEntity());
    }
}