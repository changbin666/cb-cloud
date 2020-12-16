package com.cb.cloud.demo.service.impl;

import com.cb.cloud.demo.service.Demo1FeignService;
import org.springframework.stereotype.Component;

@Component
public class Demo1FeignServiceFallback implements Demo1FeignService {

    @Override
    public String getDemo1Service() {
        return "demo1service 出错，这是备用方法";
    }
}
