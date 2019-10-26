package com.yjy.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class HelloController {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@GetMapping("/hi")
	public String hello(String name) {
		return restTemplate.getForObject("http://service-provider/hi?name=" + name, String.class);
	}
}
