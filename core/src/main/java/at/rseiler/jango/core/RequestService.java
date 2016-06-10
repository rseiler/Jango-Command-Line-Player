package at.rseiler.jango.core;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class RequestService {

    private HttpClient httpClient = new DefaultHttpClient();

    /**
     * Fetches the data from the given url.
     *
     * @param url the url which will be opened
     * @return the content
     */
    public String grabData(String url) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            HttpGet method = new HttpGet(url);
            method.addHeader("X-Requested-With", "XMLHttpRequest");
            HttpResponse httpResponse = httpClient.execute(method);

            Scanner scanner = new Scanner(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));
            while (scanner.hasNextLine()) {
                stringBuilder.append(scanner.nextLine());
            }
            scanner.close();

            EntityUtils.consume(httpResponse.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * Opens the main page to get the necessary cookies.
     */
    public void prepareConnection(String uri) throws IOException {
        EntityUtils.consume(httpClient.execute(new HttpGet(uri)).getEntity());
//        EntityUtils.consume(httpClient.execute(new HttpGet("http://www.jango.com/stations/" + stationId + "/tunein")).getEntity());
    }
}