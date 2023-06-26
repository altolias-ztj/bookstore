package com.bookstore.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("order_items")
public class OrderItem implements Serializable {
    @TableId(type = IdType.INPUT)
    private Long itemId;
    private Long orderId;
    private Long bookId;
    private int count;
    private double price;

    public OrderItem(CartItem cartItem) {
        this.bookId = cartItem.getBook().getBookId();
        this.count = cartItem.getCount();
        this.price = cartItem.getPrice();
        this.book = cartItem.getBook();
    }

    public OrderItem() {

    }

    @TableField(exist = false)
    private Book book;
}
