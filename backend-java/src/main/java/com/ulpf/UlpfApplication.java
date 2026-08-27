package com.ulpf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class UlpfApplication {

    public static void main(String[] args) {
        SpringApplication.run(UlpfApplication.class, args);
    }
}
