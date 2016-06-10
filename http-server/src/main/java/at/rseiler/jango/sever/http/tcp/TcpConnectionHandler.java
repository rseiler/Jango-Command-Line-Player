package at.rseiler.jango.sever.http.tcp;

import at.rseiler.jango.core.SongData;
import at.rseiler.jango.core.SongService;
import org.apache.commons.io.IOUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TcpConnectionHandler extends Thread {

    private final static Pattern LENGTH_PATTERN = Pattern.compile("ID_LENGTH=(\\d+.\\d+)");
    private final Socket socket;
    private final SongService songService;

    public TcpConnectionHandler(Socket socket, SongService songService) {
        this.socket = socket;
        this.songService = songService;
    }

    @Override
    public void run() {
        try (DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
            while (true) {
                SongData songData = songService.nextSong();

                try {
                    Process process = new ProcessBuilder("mplayer", "-really-quiet", "-vo", "null", "-ao", "null", "-frames", "0", "-identify", songData.getUrl()).start();
                    Matcher matcher = LENGTH_PATTERN.matcher(IOUtils.toString(process.getInputStream()));

                    if (matcher.find()) {
                        double length = Double.parseDouble(matcher.group(1));

                        output.writeBytes("http://" + InetAddress.getLocalHost().getHostAddress() + ":8080/song/" + songData.getArtist() + " - " + songData.getSong() + ".m4p\n");
                        output.flush();

                        Thread.sleep((long) (length * 1000));
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
