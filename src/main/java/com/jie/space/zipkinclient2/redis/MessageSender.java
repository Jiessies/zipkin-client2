package com.jie.space.zipkinclient2.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

//@EnableScheduling //开启定时器功能
@Component
public class MessageSender {

    @Autowired
    private RedisTemplate redisTemplate;

//    @Scheduled(fixedRate = 2000) //间隔2s 通过StringRedisTemplate对象向redis消息队列chat频道发布消息
    public void sendMessage(){
        redisTemplate.convertAndSend("chat",String.valueOf(Math.random()));
    }
}
