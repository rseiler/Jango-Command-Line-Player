package at.rseiler.jango.sever.http.command;

import at.rseiler.jango.core.command.PlayCommand;
import at.rseiler.jango.core.song.SongData;
import at.rseiler.jango.sever.http.event.PlayEvent;
import org.springframework.context.ApplicationEventPublisher;

public class PlayCommandExec extends PlayCommand implements CommandExec {
    public PlayCommandExec() {
    }

    public PlayCommandExec(SongData songData, int time) {
        super(songData, time);
    }

    @Override
    public void execute(ApplicationEventPublisher publisher) {
        publisher.publishEvent(new PlayEvent(getSongData(), getTime()));
    }
}