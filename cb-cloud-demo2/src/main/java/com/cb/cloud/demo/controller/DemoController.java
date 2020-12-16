package com.cb.cloud.demo.controller;

import com.cb.cloud.demo.entity.DemoEntity;
import com.cb.cloud.demo.service.Demo1FeignService;
import com.cb.cloud.demo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo2")
public class DemoController {

    @Autowired
    private DemoService demoService ;

    @Autowired
    private Demo1FeignService demo1FeignService;

    @RequestMapping(value = "/get/{id}",method = RequestMethod.GET)
    public DemoEntity test(@PathVariable Integer id){
        System.out.println("id:" + id);
        return demoService.getById(id);
    }

    /**
     * 作为demo2服务提供方，对外提供一个服务
     * @return
     */
    @RequestMapping(value = "/demo2service",method = RequestMethod.GET)
    public String demo2Service(){
        return "我是demo2提供的服务";
    }

    @RequestMapping(value = "/testfeign",method = RequestMethod.GET)
    public String testFeign(){

        String str = demo1FeignService.getDemo1Service();
        if (str.equals("demo1service 出错，这是备用方法")) {
            return "原方法出错，此时返回容错备用方案结果";
        }
        return str;
    }

}
