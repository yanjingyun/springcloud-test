package com.yjy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yjy.service.Provider01Service;

@RestController
public class HelloController {

	@Autowired
	Provider01Service provider01Service;

	@GetMapping(value = "/hi")
	public String hi(@RequestParam String name) {
		return provider01Service.hiService(name);
	}
}
