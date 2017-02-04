package jango.command.mapper;

import at.rseiler.jango.core.command.PauseCommand;
import at.rseiler.jango.core.command.PlayCommand;
import fr.xebia.extras.selma.IgnoreMissing;
import fr.xebia.extras.selma.Mapper;
import fr.xebia.extras.selma.Maps;
import jango.command.PauseCommandExec;
import jango.command.PlayCommandExec;

@Mapper
public interface CommandExecMapper {
    @Maps(withIgnoreMissing = IgnoreMissing.ALL)
    PlayCommandExec asExec(PlayCommand command);

    PauseCommandExec asExec(PauseCommand command);
}
