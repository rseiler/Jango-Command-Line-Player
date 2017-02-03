package jango;

import at.rseiler.jango.core.RequestService;
import at.rseiler.jango.core.player.Player;
import at.rseiler.jango.core.song.*;
import at.rseiler.jango.core.station.StationServiceImpl;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CancellationException;

public class JangoCLPlayer {

    private static final int DEFAULT_PORT = 9888;
    private static Player PLAYER;
    private static NextSongService nextSongService;

    public static void main(String[] args) throws IOException {
        RequestService requestService = new RequestService();

        if (args.length == 1) {
            if (args[0].equals("stations")) {
                new StationServiceImpl(requestService)
                        .topStations()
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
            String[] address = args[1].split(":");
            String host = address[0];
            int port = address.length == 2 ? Integer.parseInt(address[1]) : DEFAULT_PORT;
            TcpClient tcpClient = new TcpClient(new Socket(host, port));
            tcpClient.start();
        } else {
            addShutdownHook();

            PLAYER = new Player(args[0]);
            List<Class<? extends NextSongServiceDecorator>> decorators = Arrays.asList(
                    NextSongServiceWithConsoleLogging.class,
                    NextSongServiceWithStoring.class
            );
            nextSongService = new SongServiceBuilder(new NextSongServiceImpl(requestService, args[1]))
                    .withDecorators(decorators)
                    .build();
            playNextSong();

            processCommands();
        }

        Scanner scanner = new Scanner(System.in);
        scanner.hasNextLine();
    }

    private static void playNextSong() {
        PLAYER.play(nextSongService.getNextSong())
                .whenComplete(JangoCLPlayer::whenComplete);
    }

    private static void whenComplete(SongData songData, Throwable throwable) {
        if (!(throwable instanceof CancellationException)) {
            playNextSong();
        }
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
                    PLAYER.stop();
                    break;
                case "exit":
                case "quit":
                case "q":
                case "e":
                    PLAYER.stop();
                    break inputLoop;
                default:
                    PLAYER.stop();
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
