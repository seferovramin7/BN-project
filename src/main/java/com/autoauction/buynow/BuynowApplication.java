package com.autoauction.buynow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BuynowApplication {

    public static void main(String[] args) {
        SpringApplication.run(BuynowApplication.class, args);
    }

}
