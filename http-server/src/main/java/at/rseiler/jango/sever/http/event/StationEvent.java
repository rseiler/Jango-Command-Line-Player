package at.rseiler.jango.sever.http.event;

public class StationEvent {
    private final String stationId;

    public StationEvent(String stationId) {
        this.stationId = stationId;
    }

    public String getStationId() {
        return stationId;
    }

    @Override
    public String toString() {
        return "StationEvent{" +
                "stationId=" + stationId +
                '}';
    }
}
