package jango;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
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

    private static Pattern mp3urlPattern = Pattern.compile("url\":\"(.+?)\"");
    private static Pattern idPattern = Pattern.compile("(\\d+)\\.");
    private static Pattern songPattern = Pattern.compile("song\":\"(.*?)\"");
    private static Pattern artistPattern = Pattern.compile("artist\":\"(.*?)\"");
    private static Pattern stationIdPattern = Pattern.compile("/stations/(\\d+)/tunein.*?class=\"sp_tgname\">([\\w\\d /]+)</span");
    private static HttpClient client = new DefaultHttpClient();
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static Process mplayerProcess;
    private static boolean running = true;

    /**
     * @param args
     * @throws IOException
     * @throws ClientProtocolException
     */
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

        final String pathToMplayer = args[0];
        final String stationId = args[1];

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                killMplayer();
            }
        });

        // "disables" ALL error messages
        // i didn't found any other way to disable the
        // org.apache.http.client.protocol.ResponseProcessCookies warning. if you
        // know another way, please tell me.
        System.setErr(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
            }
        }));

        // opens the main page to get the necessary cookies
        EntityUtils.consume(client.execute(new HttpGet("http://www.jango.com/stations/" + stationId + "/tunein")).getEntity());

        Thread player = new Thread() {
            @Override
            public void run() {
                while (running) {
                    try {
                        String[] arr = getIdAndMp3Url(stationId);

                        String id = arr[0];
                        String mp3url = arr[1];
                        mplayerProcess = new ProcessBuilder(pathToMplayer, "-really-quiet", mp3url).start();
                        songInfo(id);
                        mplayerProcess.waitFor();
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        };
        player.setDaemon(true);
        player.start();

        Scanner scanner = new Scanner(System.in);
        while (running && scanner.hasNextLine()) {
            String line = scanner.nextLine();
            switch (line) {
                case "pause":
                case "p":
                    OutputStream os = mplayerProcess.getOutputStream();
                    os.write("p".getBytes());
                    os.flush();
                    break;
                case "next":
                case "n":
                    killMplayer();
                    break;
                case "exit":
                case "quit":
                case "q":
                case "e":
                    running = false;
                    killMplayer();
                    break;

                default:
                    killMplayer();
                    break;
            }
        }
        scanner.close();
    }

    private static void killMplayer() {
        if (mplayerProcess != null) {
            OutputStream os = mplayerProcess.getOutputStream();
            try {
                os.write("q".getBytes());
                os.flush();
            } catch (IOException e) {
                mplayerProcess.destroy();
            }
        }
        mplayerProcess = null;
    }

    private static void usage() {
        System.out.println("usage: [path/to/mplayer stationId] [stations]");
    }

    private static void printTopStations() {
        try {
            String html = getHtml("http://www.jango.com");
            Matcher stationMatcher = stationIdPattern.matcher(html);
            while (stationMatcher.find()) {
                System.out.println(stationMatcher.group(1).trim() + "   " + stationMatcher.group(2).trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void songInfo(final String id) {
        Thread songInfo = new Thread() {

            @Override
            public void run() {
                try {
                    String song = null, artist = null;
                    String html = getHtml("http://www.jango.com/players/usd?ver=4&next=1&cb=" + id);
                    Matcher songMatcher = songPattern.matcher(html);
                    if (songMatcher.find()) {
                        song = songMatcher.group(1);
                    }
                    Matcher artistMatcher = artistPattern.matcher(html);
                    if (artistMatcher.find()) {
                        artist = artistMatcher.group(1);
                    }

                    String info = dateFormat.format(new Date()) + " | " + artist + " - " + song;
                    System.out.println(info);

                    if (artist != null && song != null) {
                        FileWriter fstream = new FileWriter("songlist.txt", true);
                        BufferedWriter out = new BufferedWriter(fstream);
                        out.write(info + "\n");
                        out.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        songInfo.setDaemon(true);
        songInfo.start();
    }

    private static String[] getIdAndMp3Url(String stationId) {
        String mp3url = null, id = null;
        String html = getHtml("http://www.jango.com/streams/" + stationId);
        Matcher mp3urlMatcher = mp3urlPattern.matcher(html);
        if (mp3urlMatcher.find()) {
            mp3url = mp3urlMatcher.group(1);

            Matcher idMatcher = idPattern.matcher(mp3url);
            if (idMatcher.find()) {
                id = idMatcher.group(1);
            }
        }

        return new String[]{id, mp3url};
    }

    private static String getHtml(String url) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            HttpGet method = new HttpGet(url);
            HttpResponse httpResponse = client.execute(method);

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
}
