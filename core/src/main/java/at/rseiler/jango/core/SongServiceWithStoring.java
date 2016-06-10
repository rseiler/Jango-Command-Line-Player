package at.rseiler.jango.core;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class SongServiceWithStoring extends SongServiceDecorator {

    public SongServiceWithStoring(SongService songService) {
        super(songService);
    }

    @Override
    public SongData nextSong() {
        SongData songData = songService.nextSong();
        String name = songData.getArtist() + " - " + songData.getSong();
        File file = new File("songs/" + name + ".m4p");

        if (!file.exists()) {
            try {
                FileUtils.copyURLToFile(new URL(songData.getUrl()), file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return new SongData(file.getAbsolutePath(), songData.getArtist(), songData.getSong());
    }
}
