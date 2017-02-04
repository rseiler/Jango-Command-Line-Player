package at.rseiler.jango.sever.http.command;

import at.rseiler.jango.core.command.StationCommand;
import at.rseiler.jango.sever.http.event.NextSongEvent;
import at.rseiler.jango.sever.http.event.StationEvent;
import org.springframework.context.ApplicationEventPublisher;

public class StationCommandExec extends StationCommand implements CommandExec {
    @Override
    public void execute(ApplicationEventPublisher publisher) {
        publisher.publishEvent(new StationEvent(getStationId()));
        publisher.publishEvent(new NextSongEvent());
    }
}
