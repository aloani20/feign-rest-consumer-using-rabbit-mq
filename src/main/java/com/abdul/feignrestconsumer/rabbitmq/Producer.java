package com.abdul.feignrestconsumer.rabbitmq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Producer {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Value("${spring.rabbitmq.myExchange}")
    private String exchange;

    @Value("${spring.rabbitmq.myRoutingKey}")
    private String routingKey;

    public void produceMsg(String msg){
        amqpTemplate.convertAndSend(exchange, routingKey, msg);
        System.out.println("Send msg = " + msg);
    }

    @Scheduled(fixedRate = 10000)
    public void produceMessageToWaitingQueue(Message message){

        if( message.getMessageProperties().getHeaders().get("retryCount") == null ){
                    message.getMessageProperties().getHeaders().put("retryCount",1);
                    message.getMessageProperties().setExpiration("5000");
        }else if( message.getMessageProperties().getHeaders().get("retryCount").equals(1)){
            message.getMessageProperties().getHeaders().put("retryCount",2);
            message.getMessageProperties().setExpiration("10000");
        }else if( message.getMessageProperties().getHeaders().get("retryCount").equals(2)){
            message.getMessageProperties().getHeaders().put("retryCount",3);
            message.getMessageProperties().setExpiration("15000");
        }else {
            message.getMessageProperties().getHeaders().put("retryCount",4);
            message.getMessageProperties().setExpiration("20000");
        }
        amqpTemplate.convertAndSend("my-waiting-exchange", "my-waiting-queue-routing-key", message);
    }

    public void sendGreeting(String name) {
        MessageProperties messageProperties = new MessageProperties();
        String greeting = "greeting "+ name;
        byte[] body = greeting.getBytes();
        Message message = new Message(body,messageProperties);
        amqpTemplate.convertAndSend(exchange, routingKey, greeting);
    }
}