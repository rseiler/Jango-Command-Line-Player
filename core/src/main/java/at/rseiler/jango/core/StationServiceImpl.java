package at.rseiler.jango.core;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StationServiceImpl implements StationService {

    private static Pattern STATION_ID_PATTERN = Pattern.compile("/stations/(\\d+)/tunein.*?class=\"sp_tgname\">([\\w\\d /]+)</span");

    private final RequestService requestService;

    public StationServiceImpl(RequestService requestService) {
        this.requestService = requestService;
    }

    /**
     * Prints the top stations from the main page.
     */
    @Override
    public List<Station> topStations() {
        List<Station> stations = new ArrayList<>();

        try {
            String html = requestService.grabData("http://www.jango.com");
            Matcher stationMatcher = STATION_ID_PATTERN.matcher(html);
            while (stationMatcher.find()) {
                stations.add(new Station(stationMatcher.group(1).trim(), stationMatcher.group(2).trim()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stations;
    }
}
