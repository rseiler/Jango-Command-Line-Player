package at.rseiler.jango.sever.http.service;

import at.rseiler.jango.core.station.Station;
import at.rseiler.jango.core.station.StationService;
import at.rseiler.jango.sever.http.config.CacheConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("cachedStationService")
public class CachedStationService implements StationService {
    private final StationService stationService;

    @Autowired
    public CachedStationService(StationService stationService) {
        this.stationService = stationService;
    }

    @Override
    @Cacheable(CacheConfig.CACHE_NAME)
    public List<Station> topStations() {
        return stationService.topStations();
    }
}
