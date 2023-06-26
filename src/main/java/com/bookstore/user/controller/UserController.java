package com.bookstore.user.controller;

import com.bookstore.admin.service.UserService;
import com.bookstore.common.entity.CartItem;
import com.bookstore.common.entity.Order;
import com.bookstore.common.entity.User;
import com.bookstore.common.entity.wx.PayResultVO;
import com.bookstore.common.util.JwtUtil;
import com.bookstore.common.util.ResponseResult;
import com.bookstore.common.util.WxUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private WxUtil wxUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/uploadCode")
    public Object uploadCode(String code) {
        String telNumber = wxUtil.getTelByCode(code);
        User user = userService.getByTelNUmber(telNumber);
        String token = jwtUtil.getToken(user);
        return new ResponseResult(200, "", token);
    }

    @GetMapping("/addToCart")
    public Object addToCart(@SessionAttribute User user, Long bookId) {
        userService.addToCart(user, bookId);
        return new ResponseResult(200, "", null);
    }

    @GetMapping("/myCart")
    public Object myCart(@SessionAttribute User user) {
        List<CartItem> list = userService.getMyCart(user);
        return new ResponseResult(200, "", list);
    }

    @GetMapping("/getMyAddress")
    public Object getMyAddress(@SessionAttribute User user) {
        return new ResponseResult(200, "", userService.getMyAddress(user.getUserId()));
    }

    @GetMapping("/order")
    public Object order(@SessionAttribute User user, Integer addressId) {
        Order order = userService.order(user, addressId);
        Map<String, Object> orderInfo = userService.createPayOrder(order, user);
        return new ResponseResult(200, "", orderInfo);
    }

    @PostMapping("/notify")
    public Object notifyPay(@RequestBody PayResultVO payResultVO) {
        System.out.println(payResultVO);
        //处理支付结果
        userService.dealPayResult(payResultVO);
        //接收成功：HTTP应答状态码需返回200或204，无需返回应答报文。
        return "";
    }

    @GetMapping("/myOrders")
    public Object myOrders(@SessionAttribute User user) {
        List<Order> orders = userService.getOrdersByUserId(user.getUserId());
        return new ResponseResult(200, "", orders);
    }
}
