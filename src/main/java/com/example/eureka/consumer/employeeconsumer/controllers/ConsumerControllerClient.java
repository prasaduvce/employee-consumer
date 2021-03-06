package com.example.eureka.consumer.employeeconsumer.controllers;

import com.example.eureka.consumer.employeeconsumer.model.Employee;
import com.example.eureka.consumer.employeeconsumer.services.RemoteCallService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class ConsumerControllerClient {

	@Autowired
	private DiscoveryClient discoveryClient;

	@Autowired
	private LoadBalancerClient loadBalancerClient;

	@Autowired
	private RemoteCallService remoteCallService;

	public void getEmployee() {
		String baseUrl = "http://localhost:8080/employee";
		RestTemplate template = new RestTemplate();
		ResponseEntity<String> responseEntity = null;

		try {
			responseEntity = template.exchange(baseUrl, HttpMethod.GET, getHeaders(), String.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		System.out.println("With rest Template ===> "+responseEntity.getBody());
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
		System.out.println("With service discovery ===> "+responseEntity.getBody());
	}

	public void getEmployeeWithZuulDiscovery() {
		List<ServiceInstance> instances = discoveryClient.getInstances("employee-zuul-service");
		ServiceInstance serviceInstance = instances.get(0);

		String baseUrl = serviceInstance.getUri().toString()+"/producer/employee";
		System.out.println("baseUrl ===> "+baseUrl);
		RestTemplate template = new RestTemplate();
		ResponseEntity<String> responseEntity = null;

		try {
			responseEntity = template.exchange(baseUrl, HttpMethod.GET, getHeaders(), String.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		System.out.println("With zuul discovery ===> "+responseEntity.getBody());
	}

	public void getEmployeeWithLoadBalancer() {
		ServiceInstance serviceInstance = loadBalancerClient.choose("employee-producer");

		String baseUrl = serviceInstance.getUri().toString()+"/employee";
		RestTemplate template = new RestTemplate();
		ResponseEntity<String> responseEntity = null;

		try {
			responseEntity = template.exchange(baseUrl, HttpMethod.GET, getHeaders(), String.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		System.out.println("With load balancer ribbon ====> "+responseEntity.getBody());
	}

	public void getEmployeeWithFeignClient() {
		Employee employee = remoteCallService.getData();
		System.out.println("employee with feighn client ===> "+employee);
	}

	private static HttpEntity<?> getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		return new HttpEntity<>(headers);
	}
}
