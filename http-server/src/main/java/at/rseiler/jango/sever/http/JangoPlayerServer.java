package at.rseiler.jango.sever.http;

import at.rseiler.jango.core.station.StationService;
import at.rseiler.jango.core.station.StationServiceGrabber;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

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
        return new StationServiceGrabber("http://www.jango.com");
    }

    @Configuration
    public class WebMvcConfig extends WebMvcConfigurerAdapter {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/song/**")
                    .addResourceLocations("file:songs/");
        }
    }
}
