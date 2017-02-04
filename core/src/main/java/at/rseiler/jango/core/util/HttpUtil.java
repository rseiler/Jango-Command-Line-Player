package at.rseiler.jango.core.util;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public final class HttpUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtil.class);
    private static final HttpClient HTTP_CLIENT = HttpClientBuilder.create().build();

    /**
     * Fetches the data from the given url.
     *
     * @param url the url which will be opened
     * @return the content
     */
    public static String grabData(String url) {
        String result = "";

        try {
            HttpGet method = new HttpGet(url);
            method.addHeader("X-Requested-With", "XMLHttpRequest");
            HttpResponse httpResponse = HTTP_CLIENT.execute(method);
            result = IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8").replaceAll("\\n", "");
            EntityUtils.consume(httpResponse.getEntity());
        } catch (Exception e) {
            LOGGER.error("Failed to grab data", e);
        }

        return result;
    }

    /**
     * Opens the main page to get the necessary cookies.
     */
    public static void prepareConnection(String uri) throws IOException {
        EntityUtils.consume(HTTP_CLIENT.execute(new HttpGet(uri)).getEntity());
    }

    private HttpUtil() {
    }
}