package de.springbootbuch.messaging.amqp.film_rental;

import de.springbootbuch.messaging_amqp.film_rental.Application;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
        MessagingTestConfiguration.class,
        Application.class,
})
@AutoConfigureMessageVerifier
@SpringBootTest(properties = "stubrunner.amqp.enabled=true")
public abstract class MessagingTestBase {
    void messageIsValid () {
        System.out.println("It is valid!");
    }
}
