package com.example.eureka.consumer.employeeconsumer.services;

import com.example.eureka.consumer.employeeconsumer.model.Employee;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "employee-producer")
public interface RemoteCallService {

	@RequestMapping(method = RequestMethod.GET, value = "/employee")
	public Employee getData();
}
