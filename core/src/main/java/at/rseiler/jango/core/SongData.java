package at.rseiler.jango.core;

/**
 * Holds the data of a song.
 */
public class SongData {
    private String url;
    private String artist;
    private String song;

    public SongData(String url, String artist, String song) {
        this.url = url;
        this.artist = artist;
        this.song = song;
    }

    public String getUrl() {
        return url;
    }

    public String getArtist() {
        return artist;
    }

    public String getSong() {
        return song;
    }
}