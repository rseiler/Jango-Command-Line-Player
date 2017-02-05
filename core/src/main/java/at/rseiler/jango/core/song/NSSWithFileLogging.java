package at.rseiler.jango.core.song;

import at.rseiler.jango.core.service.decorator.OpDec;
import at.rseiler.jango.core.util.SongUtil;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class NSSWithFileLogging implements OpDec<SongData> {
    @Override
    public SongData exec(SongData songData) {
        String songDataInfo = SongUtil.getDateTimeArtistSong(songData);

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
