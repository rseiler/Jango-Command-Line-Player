package at.rseiler.jango.clplayer.player;

import at.rseiler.jango.core.util.ObjectMapperUtil;
import at.rseiler.jango.core.command.NextSongCommand;
import at.rseiler.jango.core.command.PauseCommand;
import at.rseiler.jango.core.command.StationCommand;
import at.rseiler.jango.core.player.MPlayer;
import at.rseiler.jango.core.song.SongData;
import at.rseiler.jango.clplayer.TcpClient;

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
        tcpClient.write(ObjectMapperUtil.write(new PauseCommand()));
    }

    @Override
    public void onNext() {
        tcpClient.write(ObjectMapperUtil.write(new NextSongCommand()));
    }

    @Override
    public void onStation(String stationId) {
        tcpClient.write(ObjectMapperUtil.write(new StationCommand(stationId)));
    }

    @Override
    public void stop() {
        MPlayer.stop();
    }

    public void play(String url, double songTime) {
        MPlayer.play(new SongData(url, "", ""), songTime);
    }

    public void pause() {
        MPlayer.pause();
    }
}
