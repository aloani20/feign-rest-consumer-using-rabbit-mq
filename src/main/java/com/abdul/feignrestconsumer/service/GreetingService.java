package com.abdul.feignrestconsumer.service;

public interface GreetingService {
    String greeting(String username);
    void sendGreeting(String name);
}
