package de.springbootbuch.messaging.amqp.film_rental;

//import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import de.springbootbuch.messaging_amqp.film_rental.FilmReturnedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.MessageConverter;
//import org.springframework.cloud.stream.config.BindingServiceProperties;
import org.springframework.context.annotation.Bean;

import java.util.Map;

public class MessagingTestConfiguration {
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(
                new Queue("test.queue")).to(
                        new DirectExchange("contract-test.exchange")).with("#");
    }

//    @Bean
//    public BindingServiceProperties bindingServiceProperties() {
//        return new BindingServiceProperties();
//    }

    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(ConnectionFactory connectionFactory,
                                                                         MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames("test.queue");
        container.setMessageListener(listenerAdapter);

        return container;
    }

    private static final Logger FilmReturnedEvent_LOG = LoggerFactory
            .getLogger(FilmReturnedEvent.class);

    @RabbitListener(bindings = @QueueBinding(
            value = @org.springframework.amqp.rabbit.annotation.Queue(value = "test.queue"),
            exchange = @Exchange(value = "contract-test.exchange", ignoreDeclarationExceptions = "true")))
    public void handleFilm(FilmReturnedEvent filmReturnedEvent) {
        FilmReturnedEvent_LOG.info("Received '{}'", filmReturnedEvent.getTitle());
    }

//    @Bean(name = "returned-film-events")
//    public QueueChannel amqpInputChannel() {
//        QueueChannel result = new QueueChannel();
//        return result;
//    }
//
    @Bean
    MessageListenerAdapter filmReturnedEventListener(
            final MessageConverter filmReturnedEventConverter,
            final FilmReturnedEventReceiver filmReturnedEventReceiver
    ) {
        final MessageListenerAdapter adapter = new MessageListenerAdapter(filmReturnedEventReceiver);
        adapter.setDefaultListenerMethod("filmReturned");
        adapter.setMessageConverter(filmReturnedEventConverter);
        return adapter;
    }

    private static final Logger FilmReturnedEventReceiver_LOG = LoggerFactory
            .getLogger(FilmReturnedEventReceiver.class);

    public class FilmReturnedEventReceiver {
        public void filmReturned(FilmReturnedEvent event) {
            FilmReturnedEventReceiver_LOG.info("Film '{}' returned, billing customer...", event.getTitle());
        }
        public void filmReturned(byte[] event) {
            FilmReturnedEventReceiver_LOG.info("Film 'as byte[]' returned, billing customer...");
        }
        public void filmReturned(Map<String,Object> event) {
            FilmReturnedEventReceiver_LOG.info("Film 'as map' returned, billing customer...");
        }
    }

    @Bean
    FilmReturnedEventReceiver filmReturnedEventReceiver() {
        return new FilmReturnedEventReceiver();
    }
//
//    @Bean
//    RabbitListenerConfigurer rabbitListenerConfigurer(
//            MessageListenerAdapter filmReturnedEventListener
//    ) {
//        return registrar -> {
//            final SimpleRabbitListenerEndpoint rv
//                    = new SimpleRabbitListenerEndpoint();
//            rv.setId("returned-film-events-receiver");
//            rv.setMessageListener(filmReturnedEventListener);
//            rv.setQueueNames("returned-film-events");
//
//            registrar.registerEndpoint(rv);
//        };
//    }

}
