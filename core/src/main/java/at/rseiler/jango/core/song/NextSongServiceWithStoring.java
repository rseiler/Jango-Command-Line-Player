package at.rseiler.jango.core.song;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class NextSongServiceWithStoring extends NextSongServiceDecorator {

    public NextSongServiceWithStoring(NextSongService nextSongService) {
        super(nextSongService);
    }

    @Override
    public SongData getNextSong() {
        SongData songData = getNextSongService().getNextSong();
        File file = new File("songs/" + songData.getFileName());

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