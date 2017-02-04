package at.rseiler.jango.core.song;

import at.rseiler.jango.core.util.SongUtil;

public class NSSWithConsoleLogging extends NSSDecorator {

    public NSSWithConsoleLogging(NextSongService nextSongService) {
        super(nextSongService);
    }

    @Override
    public SongData getNextSong() {
        SongData songData = getNextSongService().getNextSong();
        String songDataInfo = SongUtil.getDateTimeArtistSong(songData);
        System.out.println(songDataInfo);
        return songData;
    }
}
