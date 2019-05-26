package com.abdul.feignrestconsumer.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Configuration
public class RabbitConfiguration {

    public final static String queueName = "myqueu";

    private @Value("${spring.rabbitmq.password}")
    String password;

    private @Value("${spring.rabbitmq.host}")
    String host;

    private @Value("${spring.rabbitmq.username}")
    String username;

    private @Value("${spring.rabbitmq.port}")
    int port;

    private @Value("${spring.rabbitmq.vHost}")
    String vHost;

    private @Value("${spring.rabbitmq.myQueue}")
    String myQueue;

    private @Value("${spring.rabbitmq.myExchange}")
    String myExchange;

    private @Value("${spring.rabbitmq.myDeadLetterExchange}")
    String myDeadLetterExchange;

    private @Value("${spring.rabbitmq.myRoutingKey}")
    String myRoutingKey;

    private @Value("${spring.rabbitmq.myDeadLetterQueue}")
    String myDeadLetterQueue;

    private @Value("${spring.rabbitmq.myDeadLetterRoutingKey}")
    String myDeadLetterRoutingKey;

    private @Value("${spring.rabbitmq.myWaitingQueue}")
    String myWaitingQueue;

    private @Value("${spring.rabbitmq.myWaitingExchange}")
    String myWaitingExchange;

    private @Value("${spring.rabbitmq.myWaitingQueueRoutingKey}")
    String myWaitingQueueRoutingKey;

    private @Value("${spring.rabbitmq.retryExchange}")
    String retryExchange;

    @Bean
    public ConnectionFactory connectionFactory() {

        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(host);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setPort(port);
        connectionFactory.setVirtualHost(vHost);
        return connectionFactory;
    }

    @Bean
    public DirectExchange myExchange(){
        return new DirectExchange(myExchange);
    }

    @Bean
    public DirectExchange myDeadLetterExchange(){
        return new DirectExchange(myDeadLetterExchange);
    }

    @Bean
    public DirectExchange myWaitingExchange(){ return new DirectExchange(myWaitingExchange); }

    @Bean
    public DirectExchange retryExchange(){
        return new DirectExchange(retryExchange);
    }


    @Bean
    public Queue myDeadLetterQueue() { return QueueBuilder.durable(myDeadLetterQueue).build();}

    @Bean
    public Queue myWaitingQueue() {
        return QueueBuilder.durable(myWaitingQueue)
                .withArgument("x-dead-letter-exchange", retryExchange)
                .withArgument("x-dead-letter-routing-key", myWaitingQueueRoutingKey)
                .withArgument("x-message-ttl",86400000)
                .build();
    }

    @Bean
    public Queue myQueue() {
        return QueueBuilder.durable(myQueue)
                .withArgument("x-dead-letter-exchange", myDeadLetterExchange)
                .withArgument("x-dead-letter-routing-key", myDeadLetterRoutingKey)
                .build();
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Binding bindingMyExchange() {
        return BindingBuilder.bind(myQueue()).to(myExchange()).with(myRoutingKey);
    }

    @Bean
    public Binding bindingMyWaitingExchange() { return BindingBuilder.bind(myWaitingQueue()).to(myWaitingExchange()).with(myWaitingQueueRoutingKey); }

    @Bean
    public Binding bindingMyDeadLetterExchange() { return BindingBuilder.bind(myDeadLetterQueue()).to(myDeadLetterExchange()).with(myDeadLetterRoutingKey); }

    @Bean
    public Binding bindingRetryExchangeToWaitingQueue() { return BindingBuilder.bind(myWaitingQueue()).to(retryExchange()).with(myWaitingQueueRoutingKey); }

    @Bean
    public Binding bindingRetryExchangeToMyQueue() { return BindingBuilder.bind(myQueue()).to(retryExchange()).with(myWaitingQueueRoutingKey); }

    @Bean
    public AmqpAdmin amqpAdmin() {
         RabbitAdmin rabbitAdmin= new RabbitAdmin(connectionFactory());

         rabbitAdmin.declareExchange(myExchange());
         rabbitAdmin.declareExchange(myDeadLetterExchange());
         rabbitAdmin.declareExchange(retryExchange());
         rabbitAdmin.declareExchange(myWaitingExchange());

         rabbitAdmin.declareQueue(myQueue());
         rabbitAdmin.declareQueue(myWaitingQueue());
         rabbitAdmin.declareQueue(myDeadLetterQueue());

         rabbitAdmin.declareBinding(bindingMyExchange());
         rabbitAdmin.declareBinding(bindingMyWaitingExchange());
         rabbitAdmin.declareBinding(bindingMyDeadLetterExchange());
         rabbitAdmin.declareBinding(bindingRetryExchangeToWaitingQueue());
         rabbitAdmin.declareBinding(bindingRetryExchangeToMyQueue());

        return rabbitAdmin;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }
}
