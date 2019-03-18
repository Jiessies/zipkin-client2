package com.jie.space.zipkinclient2.controller;

import com.jie.space.zipkinclient2.redis.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/msg")
public class MessageController {

    @Autowired
    private MessageSender messageSender;

    @RequestMapping("/sendMsg")
    public String service1() throws Exception {
        messageSender.sendMessage();
        return "success";
    }
}
