package at.rseiler.jango.core;

public class SongServiceDecorator implements SongService {

    protected final SongService songService;

    public SongServiceDecorator(SongService songService) {
        this.songService = songService;
    }

    @Override
    public SongData nextSong() {
        return songService.nextSong();
    }
}
