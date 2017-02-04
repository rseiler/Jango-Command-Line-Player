package at.rseiler.jango.core;

import at.rseiler.jango.core.song.SongData;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class SongUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static String getDateTimeArtistSong(SongData songData) {
        return DATE_TIME_FORMATTER.format(LocalDateTime.now()) + " | " + songData.getArtistSongName();
    }

    private SongUtil() {
    }
}
