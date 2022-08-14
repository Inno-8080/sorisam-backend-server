package com.sparta.sorisam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SorisamApplication {

    public static void main(String[] args) {
        SpringApplication.run(SorisamApplication.class, args);
    }

}
