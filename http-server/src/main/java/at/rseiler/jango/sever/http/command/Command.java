package at.rseiler.jango.sever.http.command;

public class Command {

    private final String id;
    private final Object data;

    public Command(Object data) {
        this.id = data.getClass().getSimpleName();
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public Object getData() {
        return data;
    }
}
