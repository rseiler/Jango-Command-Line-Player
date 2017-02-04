package at.rseiler.jango.core.command;

import at.rseiler.jango.core.fortest.CommandUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static at.rseiler.jango.core.fortest.CommandUtil.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


public class SerializationDeserializationTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    public void serialization() throws Exception {
        assertThat(OBJECT_MAPPER.readValue(CommandUtil.JSON_NEXT, Command.class), is(createNextSongCommand()));
        assertThat(OBJECT_MAPPER.readValue(CommandUtil.JSON_PAUSE, Command.class), is(createPauseCommand()));
        assertThat(OBJECT_MAPPER.readValue(CommandUtil.JSON_PLAY, Command.class), is(createPlayCommand()));
        assertThat(OBJECT_MAPPER.readValue(CommandUtil.JSON_STATION, Command.class), is(createStationCommand()));
    }

    @Test
    public void deserialize() throws Exception {
        assertThat(OBJECT_MAPPER.writeValueAsString(createNextSongCommand()), is(CommandUtil.JSON_NEXT));
        assertThat(OBJECT_MAPPER.writeValueAsString(createPauseCommand()), is(CommandUtil.JSON_PAUSE));
        assertThat(OBJECT_MAPPER.writeValueAsString(createPlayCommand()), is(CommandUtil.JSON_PLAY));
        assertThat(OBJECT_MAPPER.writeValueAsString(createStationCommand()), is(CommandUtil.JSON_STATION));
    }


}