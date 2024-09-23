package org.huydd.bus_ticket_Ecommercial_platform;

import org.huydd.bus_ticket_Ecommercial_platform.dtos.SeatDTO;
import org.huydd.bus_ticket_Ecommercial_platform.mappers.SeatDTOMapper;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.Seat;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.Trip;
import org.huydd.bus_ticket_Ecommercial_platform.repositories.TripRepository;
import org.huydd.bus_ticket_Ecommercial_platform.services.TripSeatInfoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootApplication
@EnableMongoRepositories
public class BusTicketEcommercialPlatformApplication {
	public static void main(String[] args) {
		SpringApplication.run(BusTicketEcommercialPlatformApplication.class, args);
	}


}
