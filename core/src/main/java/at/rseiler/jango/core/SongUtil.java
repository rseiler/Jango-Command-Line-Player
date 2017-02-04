package at.rseiler.jango.core;

import at.rseiler.jango.core.song.SongData;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class SongUtil {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static String getDateTimeArtingSong(SongData songData) {
        return formatter.format(LocalDateTime.now()) + " | " + songData.getArtistSongName();
    }

    private SongUtil() {
    }
}
