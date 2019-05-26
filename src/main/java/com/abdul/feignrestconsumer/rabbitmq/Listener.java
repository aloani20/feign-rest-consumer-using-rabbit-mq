package com.abdul.feignrestconsumer.rabbitmq;


import com.abdul.feignrestconsumer.client.GreetingClient;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
//import org.springframework.messaging.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

@Component
public class Listener {

    @Autowired
    Producer producer;

    @Autowired
    GreetingClient greetingClient;

    private final CountDownLatch latch = new CountDownLatch(1);

    @RabbitListener(queues ="${spring.rabbitmq.myQueue}")
    public void receive(Message message , Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag ) throws IOException{
        List<Map<String, ?>> xDeathHeader;
        System.out.println( "expiration  :"+message.getMessageProperties().getExpiration());
        System.out.println( " Time :"+  LocalDateTime.now());
        System.out.println("\n [x] Received '" + message.getBody().toString() + "'");

        String greeting = new String(message.getBody()); //.toString();
        System.out.println("greeting :"+ greeting);

        String result = greetingClient.greeting(greeting);

        if( result != null ){
            System.out.println("Message was consumed successfully");
            channel.basicAck(tag, false);
        }else {
            if( message.getMessageProperties().getHeaders() != null ){
                if( message.getMessageProperties().getHeaders().get("x-death") != null ){
                    xDeathHeader = (List<Map<String, ?>>) message.getMessageProperties().getHeaders().get("x-death");
                    System.out.println(" x death count :"+ xDeathHeader.get(0).get("count").toString());
                    if(xDeathHeader.get(0).get("count").equals(4L)){
                        channel.basicReject(tag,false);
//                        channel.basicReject(tag,false);
                    }else {
                        producer.produceMessageToWaitingQueue(message);
                        channel.basicAck(tag, false);
                    }
                }else {
                    producer.produceMessageToWaitingQueue(message);
                    channel.basicAck(tag, false);
                }
            }else {
                producer.produceMessageToWaitingQueue(message);
                channel.basicAck(tag, false);
            }
        }
    }
}
