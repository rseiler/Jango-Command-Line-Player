package at.rseiler.jango.core.song;

import at.rseiler.jango.core.service.decorator.OpDec;
import at.rseiler.jango.core.util.SongUtil;

public class NSSWithConsoleLogging implements OpDec<SongData> {
    @Override
    public SongData exec(SongData songData) {
        String songDataInfo = SongUtil.getDateTimeArtistSong(songData);
        System.out.println(songDataInfo);
        return songData;
    }
}
