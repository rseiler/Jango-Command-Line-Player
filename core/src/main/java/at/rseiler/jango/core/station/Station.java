package at.rseiler.jango.core.station;

public class Station {

    private final String name;
    private final String id;

    public Station(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
