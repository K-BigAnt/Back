package com.bigant.gaeme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GaemeApplication {

    public static void main(String[] args) {
        SpringApplication.run(GaemeApplication.class, args);
    }

}
