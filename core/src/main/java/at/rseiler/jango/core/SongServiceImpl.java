package at.rseiler.jango.core;

import com.google.gson.Gson;

import java.io.IOException;

public class SongServiceImpl implements SongService {

    private static Gson GSON = new Gson();
    private final RequestService requestService;
    private final String uri;

    public SongServiceImpl(RequestService requestService, String uri) throws IOException {
        this.requestService = requestService;
        this.uri = uri;

        requestService.prepareConnection(uri);
    }

    public SongData nextSong() {
        return GSON.fromJson(requestService.grabData(uri), SongData.class);
    }
}
