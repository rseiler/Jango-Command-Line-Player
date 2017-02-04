package at.rseiler.jango.sever.http.command;

import at.rseiler.jango.core.command.NextSongCommand;
import at.rseiler.jango.sever.http.event.NextSongEvent;
import org.springframework.context.ApplicationEventPublisher;

public class NextSongCommandExec extends NextSongCommand implements CommandExec {
    @Override
    public void execute(ApplicationEventPublisher publisher) {
        publisher.publishEvent(new NextSongEvent());
    }
}
