package com.yjy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableEurekaClient
@RestController
public class ServiceHi02Application {

	public static void main(String[] args) {
		SpringApplication.run(ServiceHi02Application.class, args);
	}

	@Value("${server.port}")
	String port;

	@RequestMapping("/hi")
	public String home(@RequestParam(value = "name", defaultValue = "forezp") String name) {
		return "hi " + name + " ,i am from port:" + port;
	}

	@RequestMapping("/timeout")
	public String timeout() {
		try {
			// 睡5秒，网关Hystrix3秒超时，会触发熔断降级操作
			Thread.sleep(5000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "timeout";
	}
}
