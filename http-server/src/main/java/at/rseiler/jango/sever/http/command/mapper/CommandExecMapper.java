package at.rseiler.jango.sever.http.command.mapper;

import at.rseiler.jango.core.command.NextSongCommand;
import at.rseiler.jango.core.command.PauseCommand;
import at.rseiler.jango.core.command.PlayCommand;
import at.rseiler.jango.core.command.StationCommand;
import at.rseiler.jango.sever.http.command.NextSongCommandExec;
import at.rseiler.jango.sever.http.command.PauseCommandExec;
import at.rseiler.jango.sever.http.command.PlayCommandExec;
import at.rseiler.jango.sever.http.command.StationCommandExec;
import fr.xebia.extras.selma.IgnoreMissing;
import fr.xebia.extras.selma.Mapper;
import fr.xebia.extras.selma.Maps;

@Mapper
public interface CommandExecMapper {
    @Maps(withIgnoreMissing = IgnoreMissing.ALL)
    PlayCommandExec asExec(PlayCommand command);

    PauseCommandExec asExec(PauseCommand command);

    NextSongCommandExec asExec(NextSongCommand command);

    StationCommandExec asExec(StationCommand command);
}
