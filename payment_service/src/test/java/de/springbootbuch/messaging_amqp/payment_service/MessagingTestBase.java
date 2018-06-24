package de.springbootbuch.messaging_amqp.payment_service;

import org.junit.runner.RunWith;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {Application.class, MessagingTestConfiguration.class})
@AutoConfigureMessageVerifier
//@AutoConfigureStubRunner
@SpringBootTest(properties = "stubrunner.amqp.enabled=true")
public class MessagingTestBase {
    public void messageIsValid() {

    }
}
