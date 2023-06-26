package com.bookstore.common.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class CartItem implements Serializable {
    private Book book;
    private int count;
    private double price;
}
