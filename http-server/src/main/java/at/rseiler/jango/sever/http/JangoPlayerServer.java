package at.rseiler.jango.sever.http;

import at.rseiler.jango.core.station.StationService;
import at.rseiler.jango.core.station.StationServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@ComponentScan("at.rseiler.jango.sever.http")
public class JangoPlayerServer {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(JangoPlayerServer.class, args);
    }

    @Bean
    StationService stationService() {
        return new StationServiceImpl();
    }
}
