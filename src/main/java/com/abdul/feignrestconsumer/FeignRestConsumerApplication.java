package com.abdul.feignrestconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
//@EnableCircuitBreaker
//@EnableHystrixDashboard
@EnableFeignClients
public class FeignRestConsumerApplication {
	public static void main(String[] args) {
		SpringApplication.run(FeignRestConsumerApplication.class, args);
	}

//	@Bean
//	public Contract useFeignAnnotations() {
//		return new Contract.Default();
//	}
}
