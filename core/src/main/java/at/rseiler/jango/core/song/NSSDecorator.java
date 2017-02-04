package at.rseiler.jango.core.song;

public class NSSDecorator implements NextSongService {
    private final NextSongService nextSongService;

    NSSDecorator(NextSongService nextSongService) {
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
