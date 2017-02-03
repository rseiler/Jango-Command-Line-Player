package at.rseiler.jango.core.song;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NextSongServiceWithConsoleLogging extends NextSongServiceDecorator {

    public NextSongServiceWithConsoleLogging(NextSongService nextSongService) {
        super(nextSongService);
    }

    @Override
    public SongData getNextSong() {
        SongData songData = getNextSongService().getNextSong();

        String info = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()) + " | " + songData.getArtist() + " - " + songData.getSong();
        System.out.println(info);

        try (FileOutputStream fos = new FileOutputStream("songlist.txt", true);
             OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
             BufferedWriter out = new BufferedWriter(osw)) {
            out.write(info + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return songData;
    }
}
