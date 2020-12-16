package com.cb.cloud.demo.controller;

import cn.hutool.json.JSONObject;
import com.cb.cloud.demo.entity.DemoEntity;
import com.cb.cloud.demo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/demo1")
public class DemoController {

    @Autowired
    private DemoService demoService ;

    int i=0;

    @RequestMapping(value = "/get/{id}",method = RequestMethod.GET)
    public DemoEntity test(@PathVariable Integer id){
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String userStr = request.getHeader("user");
        JSONObject userJsonObject = new JSONObject(userStr);
        System.out.println(userStr);
        System.out.println(userJsonObject.getStr("userName"));
        System.out.println(userJsonObject.getStr("userID"));
        return demoService.getById(id);
    }

    @RequestMapping(value = "/demo1service",method = RequestMethod.GET)
    public String demo1Service(){
        i++; if (i % 3 == 0) { throw new RuntimeException(); }
        return "我是demo1提供的服务";
    }

}
