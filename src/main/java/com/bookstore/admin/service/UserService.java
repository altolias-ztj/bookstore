package com.bookstore.admin.service;

import com.bookstore.common.entity.Addr;
import com.bookstore.common.entity.CartItem;
import com.bookstore.common.entity.Order;
import com.bookstore.common.entity.User;
import com.bookstore.common.entity.wx.PayResultVO;

import java.util.List;
import java.util.Map;

public interface UserService{

    User login(String name,String password);

    User getByUserId(Long id);

    List<CartItem> getMyCart(User user);

    void addToCart(User user, Long bookId);
    List<Addr> getMyAddress(Long userId);

    User getByTelNUmber(String telNumber);

    Order order(User user, Integer addressId);

    void dealPayResult(PayResultVO payResultVO);

    List<Order> getOrdersByUserId(Long userId);

    Map<String, Object> createPayOrder(Order order, User user);

    void cancelIfNotPa(Order order);

    User getByWxId(String userName);
}
