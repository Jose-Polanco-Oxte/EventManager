package jpolanco.applicationcore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EntityScan(basePackages = {"jpolanco.domainmodel"})
public class ApplicationCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationCoreApplication.class, args);
    }

}
