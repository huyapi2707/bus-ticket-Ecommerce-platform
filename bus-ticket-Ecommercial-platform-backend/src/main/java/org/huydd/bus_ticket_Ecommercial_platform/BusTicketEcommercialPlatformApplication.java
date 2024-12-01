package org.huydd.bus_ticket_Ecommercial_platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
@EnableCaching
public class BusTicketEcommercialPlatformApplication {
	public static void main(String[] args) {
		SpringApplication.run(BusTicketEcommercialPlatformApplication.class, args);
	}

}
