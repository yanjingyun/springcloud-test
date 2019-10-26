package com.yjy.inter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value="service-provider", fallback = ServiceHelloHystric.class)
public interface ServiceHello {

	@RequestMapping("/hi")
	String hello(@RequestParam(value = "name") String name);
}
