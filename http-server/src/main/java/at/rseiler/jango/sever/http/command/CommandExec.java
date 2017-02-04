package at.rseiler.jango.sever.http.command;

import org.springframework.context.ApplicationEventPublisher;

public interface CommandExec {
    void execute(ApplicationEventPublisher publisher);
}
