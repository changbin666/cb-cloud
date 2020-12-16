package com.cb.cloud.demo.service;

import com.cb.cloud.demo.service.impl.Demo1FeignServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value="cb-demo1-service",fallback = Demo1FeignServiceFallback.class)//声明要调用的实例的application name
public interface Demo1FeignService {

    //直接mapping到demo1中需要调用的地址
    @GetMapping(value = "/demo1/demo1service")
    String getDemo1Service();
}

