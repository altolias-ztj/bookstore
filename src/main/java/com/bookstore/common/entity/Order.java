package com.bookstore.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@TableName("orders")
public class Order implements Serializable {
    @TableId(type = IdType.INPUT)
    private Long orderId;
    private int addressId;
    private Date createTime;
    private double orderPrice;
    private int state;
    private Long userId;
    @TableField(exist = false)
    private List<OrderItem> orderItems;
}
