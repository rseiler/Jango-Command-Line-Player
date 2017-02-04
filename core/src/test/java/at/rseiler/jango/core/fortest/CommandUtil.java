package at.rseiler.jango.core.fortest;

import at.rseiler.jango.core.command.NextSongCommand;
import at.rseiler.jango.core.command.PauseCommand;
import at.rseiler.jango.core.command.PlayCommand;
import at.rseiler.jango.core.command.StationCommand;
import at.rseiler.jango.core.song.SongData;

public final class CommandUtil {
    public static final String JSON_NEXT = "{\"command\":\"next\"}";
    public static final String JSON_STATION = "{\"command\":\"station\",\"stationId\":\"123\"}";
    public static final String JSON_PAUSE = "{\"command\":\"pause\"}";
    public static final String JSON_PLAY = "{\"command\":\"play\",\"songData\":{\"url\":\"http://localhost/song/song.m4p\",\"artist\":null,\"song\":null},\"time\":1.0}";

    public static NextSongCommand createNextSongCommand() {
        return new NextSongCommand();
    }

    public static PauseCommand createPauseCommand() {
        return new PauseCommand();
    }

    public static PlayCommand createPlayCommand() {
        return new PlayCommand(new SongData("http://localhost/song/song.m4p", null, null), 1.0);
    }

    public static StationCommand createStationCommand() {
        return new StationCommand("123");
    }

    private CommandUtil() {
    }
}
