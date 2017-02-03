package at.rseiler.jango.core.song;

public class NextSongServiceDecorator implements NextSongService {

    private final NextSongService nextSongService;

    NextSongServiceDecorator(NextSongService nextSongService) {
        this.nextSongService = nextSongService;
    }

    NextSongService getNextSongService() {
        return nextSongService;
    }

    @Override
    public SongData getNextSong() {
        return nextSongService.getNextSong();
    }
}
