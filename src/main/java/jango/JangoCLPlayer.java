package jango;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JangoCLPlayer {

    private static Pattern STATION_ID_PATTERN = Pattern.compile("/stations/(\\d+)/tunein.*?class=\"sp_tgname\">([\\w\\d /]+)</span");
    private static HttpClient HTTP_CLIENT = new DefaultHttpClient();
    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static Gson GSON = new Gson();
    private static Process MPLAYER_PROCESS;
    private static boolean RUNNING = true;

    public static void main(String[] args) throws IOException {
        if (args.length == 1) {
            if (args[0].equals("stations")) {
                printTopStations();
            } else {
                usage();
            }
            return;
        }
        if (args.length != 2) {
            usage();
            return;
        }

        addShutdownHook();
        prepareConnection(args[1]);
        playSongs(args[0], args[1]);
        processCommands();
    }

    /**
     * Prints the usage command.
     */
    private static void usage() {
        System.out.println("usage: [path/to/mplayer stationId] [stations]");
    }

    /**
     * Waits and handles input from the console.
     */
    private static void processCommands() throws IOException {
        Scanner scanner = new Scanner(System.in);
        while (RUNNING && scanner.hasNextLine()) {
            String line = scanner.nextLine();
            switch (line) {
                case "pause":
                case "p":
                    OutputStream os = MPLAYER_PROCESS.getOutputStream();
                    os.write("p".getBytes());
                    os.flush();
                    break;
                case "next":
                case "n":
                    killPlayer();
                    break;
                case "exit":
                case "quit":
                case "q":
                case "e":
                    RUNNING = false;
                    killPlayer();
                    break;
                default:
                    killPlayer();
                    break;
            }
        }
        scanner.close();
    }

    /**
     * Start to play the songs from the station.
     *
     * @param pathToMplayer the path to the mplayer
     * @param stationId     the id of the station
     */
    private static void playSongs(final String pathToMplayer, final String stationId) {
        Thread player = new Thread() {
            @Override
            public void run() {
                while (RUNNING) {
                    try {
                        SongData songData = getSongData(stationId);

                        String info = DATE_FORMAT.format(new Date()) + " | " + songData.getArtist() + " - " + songData.getSong();
                        System.out.println(info);
                        FileWriter fstream = new FileWriter("songlist.txt", true);
                        BufferedWriter out = new BufferedWriter(fstream);
                        out.write(info + "\n");
                        out.close();

                        MPLAYER_PROCESS = new ProcessBuilder(pathToMplayer, "-really-quiet", songData.getUrl()).start();
                        MPLAYER_PROCESS.waitFor();
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }

            private SongData getSongData(String stationId) {
                return GSON.fromJson(grabData("http://www.jango.com/streams/info?stid=" + stationId), SongData.class);
            }
        };
        player.setDaemon(true);
        player.start();
    }

    /**
     * Quites the mplayer.
     */
    private static void killPlayer() {
        if (MPLAYER_PROCESS != null) {
            OutputStream os = MPLAYER_PROCESS.getOutputStream();
            try {
                os.write("q".getBytes());
                os.flush();
            } catch (IOException e) {
                MPLAYER_PROCESS.destroy();
            }
        }
    }

    /**
     * Prints the top stations from the main page.
     */
    private static void printTopStations() {
        try {
            String html = grabData("http://www.jango.com");
            Matcher stationMatcher = STATION_ID_PATTERN.matcher(html);
            while (stationMatcher.find()) {
                System.out.println(stationMatcher.group(1).trim() + "   " + stationMatcher.group(2).trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Fetches the data from the given url.
     *
     * @param url the url which will be opened
     * @return the content
     */
    private static String grabData(String url) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            HttpGet method = new HttpGet(url);
            method.addHeader("X-Requested-With", "XMLHttpRequest");
            HttpResponse httpResponse = HTTP_CLIENT.execute(method);

            Scanner scanner = new Scanner(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));
            while (scanner.hasNextLine()) {
                stringBuilder.append(scanner.nextLine());
            }
            scanner.close();

            EntityUtils.consume(httpResponse.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * Opens the main page to get the necessary cookies.
     */
    private static void prepareConnection(String stationId) throws IOException {
        EntityUtils.consume(HTTP_CLIENT.execute(new HttpGet("http://www.jango.com/stations/" + stationId + "/tunein")).getEntity());
    }

    /**
     * Kills the player if the program is shutdown.
     */
    private static void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                killPlayer();
            }
        });
    }

    /**
     * Holds the data of a song.
     */
    private static class SongData {
        private String url;
        private String artist;
        private String song;

        private SongData(String url, String artist, String song) {
            this.url = url;
            this.artist = artist;
            this.song = song;
        }

        String getUrl() {
            return url;
        }

        String getArtist() {
            return artist;
        }

        String getSong() {
            return song;
        }
    }
}
