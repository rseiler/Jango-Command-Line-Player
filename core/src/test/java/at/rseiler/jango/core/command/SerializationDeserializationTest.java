package at.rseiler.jango.core.command;

import at.rseiler.jango.core.song.SongData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


public class SerializationDeserializationTest {
    private static final String JSON_NEXT = "{\"command\":\"next\"}";
    private static final String JSON_STATION = "{\"command\":\"station\",\"stationId\":\"123\"}";
    private static final String JSON_PAUSE = "{\"command\":\"pause\"}";
    private static final String JSON_PLAY = "{\"command\":\"play\",\"songData\":{\"url\":\"http://localhost/song/song.mp4\",\"artist\":null,\"song\":null},\"time\":1.0}";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    public void serialization() throws Exception {
        assertThat(OBJECT_MAPPER.readValue(JSON_NEXT, Command.class), is(createNextSongCommand()));
        assertThat(OBJECT_MAPPER.readValue(JSON_PAUSE, Command.class), is(createPauseCommand()));
        assertThat(OBJECT_MAPPER.readValue(JSON_PLAY, Command.class), is(createPlayCommand()));
        assertThat(OBJECT_MAPPER.readValue(JSON_STATION, Command.class), is(createStationCommand()));
    }

    @Test
    public void deserialize() throws Exception {
        assertThat(OBJECT_MAPPER.writeValueAsString(createNextSongCommand()), is(JSON_NEXT));
        assertThat(OBJECT_MAPPER.writeValueAsString(createPauseCommand()), is(JSON_PAUSE));
        assertThat(OBJECT_MAPPER.writeValueAsString(createPlayCommand()), is(JSON_PLAY));
        assertThat(OBJECT_MAPPER.writeValueAsString(createStationCommand()), is(JSON_STATION));
    }

    private NextSongCommand createNextSongCommand() {
        return new NextSongCommand();
    }

    private PauseCommand createPauseCommand() {
        return new PauseCommand();
    }

    private PlayCommand createPlayCommand() {
        return new PlayCommand(new SongData("http://localhost/song/song.mp4", null, null), 1.0);
    }

    private StationCommand createStationCommand() {
        return new StationCommand("123");
    }
}