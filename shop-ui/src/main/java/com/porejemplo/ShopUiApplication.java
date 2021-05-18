package com.porejemplo;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

@SpringBootApplication
public class ShopUiApplication {

    @Bean
    Queue shopQueue() {
        return new Queue("shop.queue", false);
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange("users.exchange");
    }

    @Bean
    Binding firstBinding(Queue shopQueue, DirectExchange exchange) {
        return BindingBuilder
                .bind(shopQueue)
                .to(exchange)
                .with("shop"); // routing key
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    public static void main(String[] args) {
        SpringApplication.run(ShopUiApplication.class, args);
    }

}
