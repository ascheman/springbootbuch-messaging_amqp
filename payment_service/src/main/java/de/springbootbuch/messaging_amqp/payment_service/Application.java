package de.springbootbuch.messaging_amqp.payment_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerEndpoint;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jackson.JsonComponentModule;
import org.springframework.context.annotation.Bean;

/**
 * Part of springbootbuch.de.
 *
 * @author Michael J. Simons
 * @author @rotnroll666
 */
@SpringBootApplication
public class Application {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	ObjectMapper objectMapper(
		final JsonComponentModule jsonComponentModule
	) {
		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(jsonComponentModule);
		return objectMapper;
	}

	@Bean
	FilmReturnedEventReceiver filmReturnedEventReceiver() {
		return new FilmReturnedEventReceiver();
	}
	
	@Bean
	Queue returnFilmEventsQueue() {
		return new Queue("returned-film-events", true);
	}

	@Bean
	MessageConverter filmReturnedEventConverter(
		final ObjectMapper objectMapper
	) {
		final Map<String, Class<?>> t 
			= new HashMap<>();
		t.put("de.springbootbuch.messaging_amqp.film_rental.FilmReturnedEvent", 
			FilmReturnedEvent.class);
		
		final DefaultJackson2JavaTypeMapper typeMapper
			= new DefaultJackson2JavaTypeMapper();
		typeMapper.setIdClassMapping(t);
		
		final Jackson2JsonMessageConverter converter 
			= new Jackson2JsonMessageConverter(objectMapper);
		converter.setJavaTypeMapper(typeMapper);
		return converter;
	}

	@Bean
	MessageListenerAdapter filmReturnedEventListener(
		final MessageConverter filmReturnedEventConverter,
		final FilmReturnedEventReceiver filmReturnedEventReceiver
	) {
		final MessageListenerAdapter adapter
			= new MessageListenerAdapter(
				filmReturnedEventReceiver);
		adapter
			.setDefaultListenerMethod("filmReturned");
		adapter
			.setMessageConverter(filmReturnedEventConverter);
		return adapter;
	}

	@Bean
	RabbitListenerConfigurer rabbitListenerConfigurer(
		MessageListenerAdapter filmReturnedEventListener
	) {
		return registrar -> {
			final SimpleRabbitListenerEndpoint rv
				= new SimpleRabbitListenerEndpoint();
			rv.setId("returned-film-events-receiver");
			rv.setMessageListener(filmReturnedEventListener);
			rv.setQueueNames("returned-film-events");
			
			registrar.registerEndpoint(rv);
		};
	}
}