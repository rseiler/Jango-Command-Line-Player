package at.rseiler.jango.core.song;

class ForTestNextSongService implements NextSongService {
    private static final int DEFAULT_PORT = 8080;
    private final int port;

    public ForTestNextSongService() {
        this(DEFAULT_PORT);
    }

    ForTestNextSongService(int port) {
        this.port = port;
    }

    @Override
    public SongData getNextSong() {
        return new SongData("http://localhost:" + port + "/song/song.m4p", "artist", "song");
    }
}
