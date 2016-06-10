package at.rseiler.jango.sever.http;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

import java.io.IOException;

@SpringBootApplication
@ComponentScan("at.rseiler.jango.sever.http")
@ImportResource({"classpath*:applicationContext.xml"})
public class JangoPlayerServer {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(JangoPlayerServer.class, args);
    }

}
