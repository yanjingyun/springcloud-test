package com.yjy.controller;

import com.yjy.service.HiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class HelloController {

	@Autowired
	HiService hiService;

	@Autowired
	private DiscoveryClient discoveryClient;

	@Autowired
	private LoadBalancerClient loadBalancerClient;

	// ribbon 负载均衡1：使用discoveryClient
	@GetMapping(value = "/hi1")
	public String hi1(@RequestParam(value = "name", defaultValue = "user1") String name) {
		System.out.println("ribbon 负载均衡1：使用discoveryClient");

		List<ServiceInstance> instances = discoveryClient.getInstances("service-provider");
		if (instances == null || instances.size() == 0) {
			return "无该服务";
		}
		String url = instances.get(0).getUri().toString()  + "/hi?name={name}";
		Map<String, String> param = new HashMap<>();
		param.put("name", name);
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(url, String.class, param);
	}

	// ribbon 负载均衡2：使用loadBalancerClient
	@GetMapping(value = "/hi2")
	public String hi2(@RequestParam(value = "name", defaultValue = "user1") String name) {
		System.out.println("ribbon 负载均衡2：使用loadBalancerClient");

		ServiceInstance instance = loadBalancerClient.choose("service-provider");
		if (instance == null) {
			return "无该服务";
		}
		String url = instance.getUri().toString()  + "/hi?name={name}";
		Map<String, String> param = new HashMap<>();
		param.put("name", name);
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(url, String.class, param);
	}

	// ribbon 负载均衡3：使用@LoadBalanced 注解
	@GetMapping(value = "/hi3")
	public String hi3(@RequestParam(value = "name", defaultValue = "user1") String name) {
		System.out.println("ribbon 负载均衡3：使用@LoadBalanced 注解");
		return hiService.hiService(name);
	}
}
