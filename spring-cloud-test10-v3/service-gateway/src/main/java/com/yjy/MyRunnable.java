package com.yjy;

import com.netflix.discovery.shared.Application;
import com.netflix.discovery.shared.Applications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.client.discovery.event.HeartbeatEvent;
import org.springframework.cloud.netflix.eureka.CloudEurekaClient;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.util.List;

@Configuration
public class MyRunnable implements ApplicationRunner {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @EventListener(classes = HeartbeatEvent.class)
    public void listenNacosEvent(ApplicationEvent heartbeatEvent) {
        Object source = heartbeatEvent.getSource();
        CloudEurekaClient cloudEurekaClient = (CloudEurekaClient) source;
        Applications applications = cloudEurekaClient.getApplications();
        List<Application> registeredApplications = applications.getRegisteredApplications();
        for (Application application: registeredApplications) {
            logger.info("服务上线:{}", application);
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

    }
}
