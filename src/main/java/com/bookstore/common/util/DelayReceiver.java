package com.bookstore.common.util;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "delayQueue")
public class DelayReceiver {
    @RabbitHandler
    public void receive(String content) {
        System.out.println("content:" + content);
    }
}
