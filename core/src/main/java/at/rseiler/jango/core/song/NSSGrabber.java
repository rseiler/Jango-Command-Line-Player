package at.rseiler.jango.core.song;

import at.rseiler.jango.core.service.decorator.Op;
import at.rseiler.jango.core.util.HttpUtil;
import at.rseiler.jango.core.util.ObjectMapperUtil;

import java.io.IOException;

public class NSSGrabber implements NextSongService, Op<SongData> {
    private final String url;
    private final String stationId;

    public NSSGrabber(String url, String stationId) throws IOException {
        this.url = url;
        this.stationId = stationId;

        HttpUtil.prepareConnection(url + "/stations/" + stationId + "/tunein");
    }

    @Override
    public SongData exec() {
        return getNextSong();
    }

    @Override
    public SongData getNextSong() {
        SongData songData = ObjectMapperUtil.read(HttpUtil.grabData(url + "/streams/info?stid=" + stationId), SongData.class);
        return new SongData("http:" + songData.getUrl(), songData.getArtist(), songData.getSong());
    }
}
