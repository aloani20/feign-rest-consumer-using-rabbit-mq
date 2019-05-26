package com.abdul.feignrestconsumer.hystrix;

import com.abdul.feignrestconsumer.client.GreetingClient;
import org.springframework.stereotype.Component;

@Component
public class GreetingClientFallback implements GreetingClient {

    @Override
    public String greeting(String username){
        return "error from Greeting Client Fallback";
    }

}
