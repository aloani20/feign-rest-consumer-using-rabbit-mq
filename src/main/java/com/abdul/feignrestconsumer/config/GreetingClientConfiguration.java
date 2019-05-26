package com.abdul.feignrestconsumer.config;

import feign.Feign;
import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import feign.okhttp.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class GreetingClientConfiguration {

//    @Bean
//    @Scope("prototype")
//    public Feign.Builder feignBuilder() {
//        return Feign.builder().errorDecoder(errorDecoder());
//    }
//    @Bean
//    public Feign.Builder feignBuilder() {
//        return Feign.builder().errorDecoder(errorDecoder());
//    }


    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }

    @Bean
    public OkHttpClient client() {
        return new OkHttpClient();
    }

//    @Bean
//    public RequestInterceptor requestInterceptor() {
//        return requestTemplate -> {
//            requestTemplate.header("user", "ajeje");
//            requestTemplate.header("password", "brazof");
////            requestTemplate.header("Accept", ContentType.APPLICATION_JSON.getMimeType());
//        };
//    }
}
