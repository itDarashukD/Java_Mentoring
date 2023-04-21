package com.example.eurekaclient.controller;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ClientController {

    @Autowired
    @Lazy
    private EurekaClient eurekaClient;

    @Value("${spring.application.name}")
    private String appName;

    @RequestMapping("/greeting")
    public String greeting() {
        final Application application = eurekaClient.getApplication(appName);
        final InstanceInfo instanceInfo = application.getInstances().get(0);

        final int port = instanceInfo.getPort();
        final String hostName = instanceInfo.getHostName();

        return String.format("Hello from '%s'!   on the port : %d , and host : %s",
	       application.getName(),
	       port,
	       hostName);
    }


}
