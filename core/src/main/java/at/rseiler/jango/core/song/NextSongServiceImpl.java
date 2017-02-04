package at.rseiler.jango.core.song;

import at.rseiler.jango.core.util.HttpUtil;
import at.rseiler.jango.core.util.ObjectMapperUtil;

import java.io.IOException;

public class NextSongServiceImpl implements NextSongService {

    private final String url;
    private final String stationId;

    public NextSongServiceImpl(String url, String stationId) throws IOException {
        this.url = url;
        this.stationId = stationId;

        HttpUtil.prepareConnection(url + "/stations/" + stationId + "/tunein");
    }

    @Override
    public SongData getNextSong() {
        return ObjectMapperUtil.read(HttpUtil.grabData(url + "/streams/info?stid=" + stationId), SongData.class);
    }
}
