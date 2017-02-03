package at.rseiler.jango.sever.http.controller;

import at.rseiler.jango.core.station.Station;
import at.rseiler.jango.core.station.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class WebController {

    private final StationService stationService;

    @Autowired
    public WebController(@Qualifier("cachedStationService") StationService stationService) {
        this.stationService = stationService;
    }

    @RequestMapping("/")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("stationBuckets", getStationsInBuckets());
        return modelAndView;
    }

    @RequestMapping("/test")
    public ModelAndView test() {
        return new ModelAndView("test");
    }

    private List<List<Station>> getStationsInBuckets() {
        List<Station> stations = stationService.topStations();
        List<List<Station>> buckets = new ArrayList<>();
        List<Station> bucket = new ArrayList<>();
        buckets.add(bucket);

        for (int i = 1; i < stations.size() + 1; i++) {
            bucket.add(stations.get(i - 1));

            if (i % 5 == 0) {
                bucket = new ArrayList<>();
                buckets.add(bucket);
            }
        }

        return buckets;
    }
}
