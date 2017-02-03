package jango.player;

import at.rseiler.jango.core.player.MPlayer;
import at.rseiler.jango.core.song.SongData;
import jango.TcpClient;

import java.io.IOException;
import java.net.Socket;

public class SlavePlayer implements Player {

    private final String host;
    private final int port;
    private final MPlayer MPlayer = new MPlayer();
    private TcpClient tcpClient;

    public SlavePlayer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void init() throws IOException {
        tcpClient = new TcpClient(this, new Socket(host, port));
        tcpClient.start();
    }

    @Override
    public void onPause() {
        tcpClient.write("pause");
    }

    @Override
    public void onNext() {
        tcpClient.write("next");
    }

    @Override
    public void onStation(String stationId) {
        tcpClient.write("station<>" + stationId);
    }

    @Override
    public void stop() {
        MPlayer.stop();
    }

    public void play(String url, long songTime) {
        MPlayer.play(new SongData(url, "", ""), songTime);
    }

    public void pause() {
        MPlayer.pause();
    }
}
