package com.example.eureka.consumer.employeeconsumer;

import com.example.eureka.consumer.employeeconsumer.controllers.ConsumerControllerClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients
public class EmployeeConsumerApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(EmployeeConsumerApplication.class, args);
		ConsumerControllerClient client = context.getBean(ConsumerControllerClient.class);
		System.out.println("client ===> "+client);
		client.getEmployee();
		client.getEmployeeWithDiscovery();

		for (int i=0;i<100;i++) {
			client.getEmployeeWithLoadBalancer();
		}

		client.getEmployeeWithFeignClient();
	}

	@Bean
	public ConsumerControllerClient consumerControllerClient() {
		return new ConsumerControllerClient();
	}
}
