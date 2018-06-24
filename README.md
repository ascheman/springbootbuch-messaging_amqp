# messaging_amqp

This project needs a local RabbitMQ instance. If you don't have one, you can start one via Docker and Maven:

```
./mvnw docker:run
```

It is running [https://hub.docker.com/_/rabbitmq/]() and you can log into the console
on [http://localhost:15672]() with credentials `guest` / `guest` for monitoring or administration of the broker.

## Complex scenario

Start the modules `film_rental` and `payment` via

```
cd film_rental
./mvnw spring-boot:run
```

And in another shell

```
cd payment_service
./mvnw spring-boot:run
```

You then can post to the rental service like

```
curl -X "POST" "http://localhost:8080/returnedFilms" \
     -H "Content-Type: application/json; charset=utf-8" \
     -d $'{
  "title": "One Flew Over the Cuckoo\'s Nest"
}'
```

and see the film being billed in the the payment service.