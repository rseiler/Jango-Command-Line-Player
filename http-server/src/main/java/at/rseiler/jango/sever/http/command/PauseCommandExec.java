package at.rseiler.jango.sever.http.command;

import at.rseiler.jango.core.command.PauseCommand;
import at.rseiler.jango.sever.http.event.PauseEvent;
import org.springframework.context.ApplicationEventPublisher;

public class PauseCommandExec extends PauseCommand implements CommandExec {
    @Override
    public void execute(ApplicationEventPublisher publisher) {
        publisher.publishEvent(new PauseEvent());
    }
}
