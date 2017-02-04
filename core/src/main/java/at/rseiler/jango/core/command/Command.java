package at.rseiler.jango.core.command;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import static com.fasterxml.jackson.annotation.JsonSubTypes.Type;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = Command.COMMAND_NAME)
@JsonSubTypes({
        @Type(value = PlayCommand.class, name = "play"),
        @Type(value = PauseCommand.class, name = "pause"),
        @Type(value = NextSongCommand.class, name = "next"),
        @Type(value = StationCommand.class, name = "station")
})
@JsonPropertyOrder(Command.COMMAND_NAME)
public abstract class Command {
    static final String COMMAND_NAME = "command";
}
