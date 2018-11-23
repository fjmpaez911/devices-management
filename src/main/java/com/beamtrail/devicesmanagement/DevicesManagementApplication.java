package com.beamtrail.devicesmanagement;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DevicesManagementApplication {

    public static void main(String[] args) {
        configureSystem();
        SpringApplication.run(DevicesManagementApplication.class, args);
    }

    private static void configureSystem() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}
