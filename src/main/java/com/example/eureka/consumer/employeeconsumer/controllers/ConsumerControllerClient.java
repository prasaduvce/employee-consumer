package com.example.eureka.consumer.employeeconsumer.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class ConsumerControllerClient {

	@Autowired
	private DiscoveryClient discoveryClient;

	public void getEmployee() {
		String baseUrl = "http://localhost:8080/employee";
		RestTemplate template = new RestTemplate();
		ResponseEntity<String> responseEntity = null;

		try {
			responseEntity = template.exchange(baseUrl, HttpMethod.GET, getHeaders(), String.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		System.out.println(responseEntity.getBody());
	}

	public void getEmployeeWithDiscovery() {
		List<ServiceInstance> instances = discoveryClient.getInstances("employee-producer");
		ServiceInstance serviceInstance = instances.get(0);

		String baseUrl = serviceInstance.getUri().toString()+"/employee";
		RestTemplate template = new RestTemplate();
		ResponseEntity<String> responseEntity = null;

		try {
			responseEntity = template.exchange(baseUrl, HttpMethod.GET, getHeaders(), String.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		System.out.println(responseEntity.getBody());
	}

	private static HttpEntity<?> getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		return new HttpEntity<>(headers);
	}
}
