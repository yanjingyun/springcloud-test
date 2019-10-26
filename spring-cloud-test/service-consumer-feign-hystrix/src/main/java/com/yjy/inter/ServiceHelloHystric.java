package com.yjy.inter;

import org.springframework.stereotype.Component;

@Component
public class ServiceHelloHystric implements ServiceHello {

	@Override
	public String hello(String name) {
		return name + " sorry error!";
	}

}
