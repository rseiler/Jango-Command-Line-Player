package jango.player;

import java.io.IOException;

public interface Player {
    void onPause();

    void onNext();

    void pause();

    void stop();

    void onStation(String stationId) throws IOException;
}
