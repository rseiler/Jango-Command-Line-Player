package jango;

import at.rseiler.jango.core.*;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class JangoCLPlayer {

    private static Player PLAYER;

    public static void main(String[] args) throws IOException {
        RequestService requestService = new RequestService();

        if (args.length == 1) {
            if (args[0].equals("stations")) {
                new StationServiceImpl(requestService).topStations().stream()
                        .forEach(station -> System.out.println(station.getId() + " " + station.getName()));
            } else {
                usage();
            }
            return;
        }

        if (args.length < 2) {
            usage();
            return;
        }

        if ("slave".equals(args[0])) {
            TcpClient tcpClient = new TcpClient(new Socket("localhost", 9888));
            tcpClient.start();
        } else {
            addShutdownHook();

            String stationUri = "http://www.jango.com/stations/" + args[1] + "/tunein";
            SongService songService = new SongServiceWithConsoleLogging(
                    new SongServiceWithStoring(
                            new SongServiceImpl(requestService, stationUri)
                    ));
            PLAYER = new Player(args[0], songService);
            PLAYER.playSongs();

            processCommands();
        }

        Scanner scanner = new Scanner(System.in);
        scanner.hasNextLine();
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

        inputLoop:
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            switch (line) {
                case "pause":
                case "p":
                    PLAYER.pause();
                    break;
                case "next":
                case "n":
                    PLAYER.killPlayer();
                    break;
                case "exit":
                case "quit":
                case "q":
                case "e":
                    PLAYER.stop();
                    break inputLoop;
                default:
                    PLAYER.killPlayer();
                    break;
            }
        }

        scanner.close();
    }


    /**
     * Kills the player if the program is shutdown.
     */
    private static void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                PLAYER.stop();
            }
        });
    }


}
