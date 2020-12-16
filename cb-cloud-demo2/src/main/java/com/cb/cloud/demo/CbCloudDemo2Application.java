package com.cb.cloud.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients//开启Fegin
public class CbCloudDemo2Application {

    public static void main(String[] args) {
        SpringApplication.run(CbCloudDemo2Application.class, args);
    }

}
