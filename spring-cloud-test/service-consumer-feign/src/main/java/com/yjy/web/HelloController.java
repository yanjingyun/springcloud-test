package com.yjy.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yjy.inter.ServiceHello;

@RestController
public class HelloController {

	@Autowired
	ServiceHello serviceHello;
	
	@GetMapping("/hi")
	public String hello(String name) {
		return serviceHello.hello(name);
	}
}
