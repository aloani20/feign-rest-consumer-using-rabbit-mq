package com.abdul.feignrestconsumer.hystrix;

import com.abdul.feignrestconsumer.client.GreetingClient;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class HystrixGreetingClientFallbackFactory implements FallbackFactory<GreetingClient> {

    @Override
    public GreetingClient create(Throwable cause) {
        return new GreetingClient() {
            @Override
            public String greeting(String username){
                return "error from Hystrix Greeting Client Fallback Factory";
            }
        };
    }
}
