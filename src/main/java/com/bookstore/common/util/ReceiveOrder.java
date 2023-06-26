package com.bookstore.common.util;

import com.bookstore.admin.mapper.BookMapper;
import com.bookstore.admin.mapper.OrderItemMapper;
import com.bookstore.admin.mapper.OrderMapper;
import com.bookstore.common.entity.Order;
import com.bookstore.common.entity.OrderItem;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RabbitListener(queues = {"orderQueue"})
public class ReceiveOrder {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private BookMapper bookMapper;

    @RabbitHandler
    public void receive(Order order) {
        orderMapper.insert(order);
        List<OrderItem> items = order.getOrderItems();
        for (OrderItem item : items) {
            orderItemMapper.insert(item);
            bookMapper.updateStockAndSalesAmount(item.getBookId(), item.getCount());
        }
    }
}
