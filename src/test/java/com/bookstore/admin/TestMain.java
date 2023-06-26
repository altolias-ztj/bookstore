package com.bookstore.admin;

import com.bookstore.common.util.QiniuUtil;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestMain {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue queue;

    @Autowired
    private Exchange exchange;

    @Autowired
    private QiniuUtil qiniuUtil;

    @Test
    public void test1() {
        rabbitTemplate.convertAndSend("delayExchange", "delayKey", "delay message", msg -> {
            //设置消息的延迟时间
            msg.getMessageProperties().setDelay(10000);
            return msg;
        });
    }

    @Test
    public void test2() {
        rabbitTemplate.convertAndSend("orderExchange", "cancelKey", "message from wn");
    }

    @Test
    public void test3() {
        rabbitTemplate.convertAndSend("delayExchange", "delayKey", "delay message", msg -> {
            //设置消息延迟时间
            msg.getMessageProperties().setDelay(9000);
            return msg;
        });
    }

    @Test
    public void test4() {
        System.out.println(qiniuUtil.getUploadToken());
    }
}
