package at.rseiler.jango.core.song;

import at.rseiler.jango.core.RequestService;
import com.google.gson.Gson;

import java.io.IOException;

public class NextSongServiceImpl implements NextSongService {

    private static final Gson GSON = new Gson();
    private final RequestService requestService;
    private final String stationId;

    public NextSongServiceImpl(RequestService requestService, String stationId) throws IOException {
        this.requestService = requestService;
        this.stationId = stationId;

        requestService.prepareConnection("http://www.jango.com/stations/" + stationId + "/tunein");
    }

    @Override
    public SongData getNextSong() {
        return GSON.fromJson(requestService.grabData("http://www.jango.com/streams/info?stid=" + stationId), SongData.class);
    }
}
