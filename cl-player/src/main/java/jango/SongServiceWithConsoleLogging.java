package jango;

import at.rseiler.jango.core.SongData;
import at.rseiler.jango.core.SongService;
import at.rseiler.jango.core.SongServiceDecorator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SongServiceWithConsoleLogging extends SongServiceDecorator {

    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public SongServiceWithConsoleLogging(SongService songService) {
        super(songService);
    }

    @Override
    public SongData nextSong() {
        SongData songData = songService.nextSong();

        String info = DATE_FORMAT.format(new Date()) + " | " + songData.getArtist() + " - " + songData.getSong();
        System.out.println(info);

        try {
            FileWriter fstream = new FileWriter("songlist.txt", true);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(info + "\n");
            out.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return songData;
    }
}
