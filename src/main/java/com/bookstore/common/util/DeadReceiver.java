package com.bookstore.common.util;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "deadQueue")
public class DeadReceiver {
    @RabbitHandler
    public void receive() {
        System.out.println("deadMsg");
    }
}
