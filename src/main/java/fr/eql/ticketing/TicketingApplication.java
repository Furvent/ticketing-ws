package fr.eql.ticketing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TicketingApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(TicketingApplication.class);
		app.setAdditionalProfiles("initData");
		ConfigurableApplicationContext context = app.run(args);
	}

}
