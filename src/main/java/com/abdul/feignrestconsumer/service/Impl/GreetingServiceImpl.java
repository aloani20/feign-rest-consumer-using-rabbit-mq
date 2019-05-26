package com.abdul.feignrestconsumer.service.Impl;

import com.abdul.feignrestconsumer.client.GreetingClient;
import com.abdul.feignrestconsumer.rabbitmq.Producer;
import com.abdul.feignrestconsumer.service.GreetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GreetingServiceImpl implements GreetingService {

    @Autowired
    GreetingClient greetingClient;

    @Autowired
    Producer producer;

   @Override
   public String greeting(String name){
     return greetingClient.greeting(name);
    }

    @Override
    public void sendGreeting(String name){
       producer.sendGreeting(name);
    }

}
