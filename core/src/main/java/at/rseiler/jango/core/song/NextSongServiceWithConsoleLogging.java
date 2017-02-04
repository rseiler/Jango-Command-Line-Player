package at.rseiler.jango.core.song;

import at.rseiler.jango.core.SongUtil;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class NextSongServiceWithConsoleLogging extends NextSongServiceDecorator {

    public NextSongServiceWithConsoleLogging(NextSongService nextSongService) {
        super(nextSongService);
    }

    @Override
    public SongData getNextSong() {
        SongData songData = getNextSongService().getNextSong();
        String songDataInfo = SongUtil.getDateTimeArtingSong(songData);
        System.out.println(songDataInfo);

        try (FileOutputStream fos = new FileOutputStream("songlist.txt", true);
             OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
             BufferedWriter out = new BufferedWriter(osw)) {
            out.write(songDataInfo + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return songData;
    }
}
