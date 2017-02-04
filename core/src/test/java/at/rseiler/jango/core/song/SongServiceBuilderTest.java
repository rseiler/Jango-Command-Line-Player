package at.rseiler.jango.core.song;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SongServiceBuilderTest {
    @Test
    public void test() throws Exception {
        NextSongService nextSongService = new SongServiceBuilder(new ForTestNextSongService())
                .withDecorators(NSSDecorator1.class, NSSDecorator2.class)
                .build();

        assertThat(nextSongService.getNextSong().getSong(), is("song/decorator-1/decorator-2"));
    }

    private static class NSSDecorator1 extends NSSDecorator {
        NSSDecorator1(NextSongService nextSongService) {
            super(nextSongService);
        }

        @Override
        public SongData getNextSong() {
            SongData songData = getNextSongService().getNextSong();
            return new SongData(songData.getUrl(), songData.getArtist(), songData.getSong() + "/decorator-1");
        }
    }

    private static class NSSDecorator2 extends NSSDecorator {
        NSSDecorator2(NextSongService nextSongService) {
            super(nextSongService);
        }

        @Override
        public SongData getNextSong() {
            SongData songData = getNextSongService().getNextSong();
            return new SongData(songData.getUrl(), songData.getArtist(), songData.getSong() + "/decorator-2");
        }
    }
}