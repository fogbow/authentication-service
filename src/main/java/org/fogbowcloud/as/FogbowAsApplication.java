package org.fogbowcloud.as;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;

@SpringBootApplication(exclude = RepositoryRestMvcAutoConfiguration.class)
public class FogbowAsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FogbowAsApplication.class, args);
    }
}
