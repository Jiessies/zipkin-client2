package com.jie.space.zipkinclient2.controller;

import com.jie.space.zipkinclient2.utils.YklyRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "server2")
public class ZipKinTestController2 {

    @Autowired
    private YklyRestTemplate restTemplate;

    @RequestMapping("/zipkin2")
    public String service1() throws Exception {
        Thread.sleep(500);
        return "你好,欢迎进入Zipkin的世界";
    }
}
