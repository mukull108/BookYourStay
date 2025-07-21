package com.project.HotelBookingApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HotelBookingAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelBookingAppApplication.class, args);
	}

}
