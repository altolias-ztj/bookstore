package com.bookstore.admin.conf;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitConfiguration {

    @Value("${woniu.order.maxWait}")
    private Integer maxWait;


    @Bean
    public Queue queue() {
        return QueueBuilder.durable("orderQueue").build();
    }

    @Bean
    public Exchange exchange() {
        return ExchangeBuilder.directExchange("orderExchange").build();
    }

    @Bean
    public Binding binding(Queue queue, Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("orderKey").noargs();
    }

    @Bean
    public Queue cancelQueue() {
        return QueueBuilder.durable("cancelQueue").build();
    }

    @Bean
    public Exchange cancelExchange() {
        return ExchangeBuilder.directExchange("cancelExchange").build();
    }

    @Bean
    public Binding cancelBinding(Queue cancelQueue, Exchange cancelExchange) {
        return BindingBuilder.bind(cancelQueue).to(cancelExchange).with("cancelKey").noargs();
    }

    @Bean
    public Queue deadQueue() {
        Map<String, Object> args = new HashMap<>();
        //配置一个死信队列，来实现延迟消费的功能
        //配置死信交换机
        args.put("x-dead-letter-exchange", "deadExchange");
        //配置死信路由键
        args.put("x-dead-letter-routing-key", "deadKey");
        //延迟时间
        args.put("x-message-ttl", maxWait * 1000 * 60);
        return QueueBuilder.durable("deadQueue").build();
    }

    @Bean
    public Exchange deadExchange() {
        return ExchangeBuilder.directExchange("deadExchange").build();
    }

    @Bean
    public Binding deadBinding(Queue deadQueue, Exchange deadExchange) {
        return BindingBuilder.bind(deadQueue).to(deadExchange).with("deadKey").noargs();
    }

    /**
     * 延迟队列
     *
     * @return
     */
    @Bean
    public Queue delayQueue() {
        return QueueBuilder.durable("delayQueue").build();
    }

    @Bean
    public Exchange delayExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        // 交换名  类型
        return new CustomExchange("delayExchange", "x-delayed-message", true, false, args);
    }

    @Bean
    public Binding bindingDelay(Queue delayQueue, Exchange delayExchange) {
        return BindingBuilder.bind(delayQueue).to(delayExchange).with("delayKey").noargs();
    }
}
