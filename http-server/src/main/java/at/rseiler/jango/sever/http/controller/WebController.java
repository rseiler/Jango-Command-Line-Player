package at.rseiler.jango.sever.http.controller;

import at.rseiler.jango.core.StationService;
import at.rseiler.jango.sever.http.service.PlayerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;

@Controller
public class WebController {

    private final StationService stationService;
    private final PlayerManager playerManager;

    @Autowired
    public WebController(@Qualifier("cachedStationService") StationService stationService, PlayerManager playerManager) {
        this.stationService = stationService;
        this.playerManager = playerManager;
    }

    @RequestMapping("/")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("stations", stationService.topStations());
        return modelAndView;
    }

    @RequestMapping("/station/{stationId:\\d+}")
    public ModelAndView station(@PathVariable String stationId) throws IOException {
        playerManager.play(stationId);
        ModelAndView modelAndView = new ModelAndView("station");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("/song/{song:.+}")
    public FileSystemResource song(@PathVariable String song) {
        return new FileSystemResource(new File("songs/" + song));
    }
}
