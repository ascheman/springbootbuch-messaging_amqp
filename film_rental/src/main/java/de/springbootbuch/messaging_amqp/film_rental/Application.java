package de.springbootbuch.messaging_amqp.film_rental;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Part of springbootbuch.de.
 *
 * @author Michael J. Simons
 * @author @rotnroll666
 */
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
	MessageConverter filmReturnedEventConverter() {
		return new Jackson2JsonMessageConverter();
	}
	
	@Bean
	Queue returnFilmEventsQueue() {
		return new Queue("returned-film-events", true);
	}
}