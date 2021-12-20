package com.company.addonis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AddonisApplication {

    public static void main(String[] args) {
        SpringApplication.run(AddonisApplication.class, args);
    }

}
