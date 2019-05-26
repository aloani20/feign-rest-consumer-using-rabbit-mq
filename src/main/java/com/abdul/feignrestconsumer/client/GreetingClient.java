package com.abdul.feignrestconsumer.client;


import com.abdul.feignrestconsumer.config.GreetingClientConfiguration;
import com.abdul.feignrestconsumer.hystrix.GreetingClientFallback;
import com.abdul.feignrestconsumer.hystrix.HystrixGreetingClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@FeignClient(name = "rest-producer",
        url = "http://localhost:9090/greeting",
        configuration = GreetingClientConfiguration.class,
        fallback = GreetingClientFallback.class)
//        fallbackFactory = HystrixGreetingClientFallbackFactory.class)
public interface GreetingClient {

     @RequestMapping(method = RequestMethod.GET, value = "/{username}")
     String greeting(@PathVariable("username") String username);
}
