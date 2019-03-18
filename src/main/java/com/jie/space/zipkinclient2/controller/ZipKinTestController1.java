package com.jie.space.zipkinclient2.controller;

import com.jie.space.zipkinclient2.utils.YklyRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * zipkin地址
 * https://zipkin.io/pages/quickstart.html
 */
@RestController
@RequestMapping(value = "server1")
public class ZipKinTestController1 {

    @Autowired
    private YklyRestTemplate restTemplate;

    @RequestMapping("/zipkin")
    public String service1() throws Exception {
        Thread.sleep(100);

        String response = restTemplate.lGet("http://localhost:9000/server2/zipkin2", null, null);
        return response;
    }
}
