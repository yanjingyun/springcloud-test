package com.yjy.api.oauth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yjy.api.oauth.controller.vo.User;

@RefreshScope
@RestController
class TestController {

	@Value("${message:Hello default}")
	private String message;

	@Value("${foo}")
	String foo;

	@Autowired
	private User user;

	@RequestMapping("/message")
	String getMessage() {
		return this.message;
	}

	@RequestMapping("/foo")
	public String hi() {
		return foo;
	}

	@RequestMapping("/user")
	public User user() {
		return user;
	}

}
