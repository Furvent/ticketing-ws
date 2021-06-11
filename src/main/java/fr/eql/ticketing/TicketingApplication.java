package fr.eql.ticketing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TicketingApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(TicketingApplication.class);
		app.setAdditionalProfiles("initData");
		app.run(args);
	}

}
