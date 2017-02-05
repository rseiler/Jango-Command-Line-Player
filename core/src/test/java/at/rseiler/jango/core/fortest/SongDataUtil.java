package at.rseiler.jango.core.fortest;

import at.rseiler.jango.core.song.SongData;

public final class SongDataUtil {
    public static SongData createSongData() {
        return createSongData(8080);
    }

    public static SongData createSongData(int port) {
        return new SongData("http://localhost:" + port + "/song/song.m4p", "artist", "song");
    }

    private SongDataUtil() {
    }
}
