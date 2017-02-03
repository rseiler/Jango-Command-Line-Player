package jango;

import at.rseiler.jango.core.station.StationServiceImpl;
import jango.player.LocalPlayer;
import jango.player.Player;
import jango.player.SlavePlayer;

import java.io.IOException;
import java.util.Scanner;

public final class JangoCLPlayer {

    private static final int DEFAULT_PORT = 9888;

    public static void main(String[] args) throws IOException {
        if (args.length > 0) {
            if (args[0].equals("stations")) {
                printStations();
                return;
            }

            Player player;

            if ("slave".equals(args[0])) {
                String[] address = args[1].split(":");
                String host = address[0];
                int port = address.length == 2 ? Integer.parseInt(address[1]) : DEFAULT_PORT;
                SlavePlayer slavePlayer = new SlavePlayer(host, port);
                slavePlayer.init();
                player = slavePlayer;
            } else {
                LocalPlayer localPlayer = new LocalPlayer();
                localPlayer.setStationId(args[0]);
                player = localPlayer;
            }

            addShutdownHook(player);
            processCommands(player);
        } else {
            usage();
        }
    }

    /**
     * Prints the usage command.
     */
    private static void usage() {
        System.out.println("usage: [path/to/mplayer stationId] [stations]");
    }

    private static void printStations() {
        new StationServiceImpl()
                .topStations()
                .forEach(station -> System.out.println(station.getId() + " " + station.getName()));
    }


    /**
     * Waits and handles input from the console.
     *
     * @param player the player on which the actions are called
     */
    private static void processCommands(Player player) throws IOException {
        Scanner scanner = new Scanner(System.in);

        inputLoop:
        while (scanner.hasNextLine()) {
            String[] commands = scanner.nextLine().split("\\s+");

            switch (commands[0]) {
                case "pause":
                case "p":
                    player.onPause();
                    break;
                case "next":
                case "n":
                    player.onNext();
                    break;
                case "station":
                case "s":
                    player.onStation(commands[1]);
                case "exit":
                case "quit":
                case "q":
                case "e":
                    break inputLoop;
                default:
                    player.stop();
                    break;
            }
        }

        scanner.close();
    }

    /**
     * Kills the player if the program is shutdown.
     */
    private static void addShutdownHook(Player player) {
        Runtime.getRuntime().addShutdownHook(new Thread(player::stop));
    }

    private JangoCLPlayer() {
    }
}
