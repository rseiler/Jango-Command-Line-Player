package at.rseiler.jango.core.song;

import at.rseiler.jango.core.HttpUtil;
import com.google.gson.Gson;

import java.io.IOException;

public class NextSongServiceImpl implements NextSongService {

    private static final Gson GSON = new Gson();
    private final String stationId;

    public NextSongServiceImpl(String stationId) throws IOException {
        this.stationId = stationId;

        HttpUtil.prepareConnection("http://www.jango.com/stations/" + stationId + "/tunein");
    }

    @Override
    public SongData getNextSong() {
        return GSON.fromJson(HttpUtil.grabData("http://www.jango.com/streams/info?stid=" + stationId), SongData.class);
    }
}
