package com.bookstore.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bookstore.admin.mapper.*;
import com.bookstore.admin.service.BookService;
import com.bookstore.admin.service.UserService;
import com.bookstore.common.entity.*;
import com.bookstore.common.entity.wx.PayResultVO;
import com.bookstore.common.util.RedisUtil;
import com.bookstore.common.util.SnowFlake;
import com.bookstore.common.util.WxUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private AddrMapper addrMapper;
    @Resource
    private UserMapper userMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private SnowFlake snowFlake;

    @Autowired
    private BookService bookService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private WxUtil wxUtil;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public User login(String name, String password) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("user_name", name).eq("user_password", password));
    }

    @Override
    public User getByUserId(Long id) {
        return userMapper.getByUserId(id);
    }

    @Override
    public List<CartItem> getMyCart(User user) {
        return redisUtil.getAllFromHash("cart::" + user.getUserId(), CartItem.class);
    }

    @Override
    public void addToCart(User user, Long bookId) {
        Book book = bookService.getByBookId(bookId);
        CartItem item = redisUtil.getFromHash("cart::" + user.getUserId(), "book::" + bookId, CartItem.class);
        if (item == null) {
            CartItem cartItem = new CartItem();
            cartItem.setBook(book);
            cartItem.setCount(1);
            cartItem.setPrice(book.getBookPrice());
            redisUtil.putHash("cart::" + user.getUserId(), "book::" + bookId, cartItem);
        } else {
            item.setCount(item.getCount() + 1);
            item.setPrice(item.getPrice() + item.getBook().getBookPrice());
            redisUtil.putHash("cart::" + user.getUserId(), "book::" + bookId, item);
        }
    }

    @Override
    public List<Addr> getMyAddress(Long userId) {
        return addrMapper.selectList(new QueryWrapper<Addr>().eq("user_id", userId));
    }


    @Override
    public User getByTelNUmber(String telNumber) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("user_tel", telNumber));
    }

    /**
     * 这个方法应该是一个事务方法
     * 用户下单：
     * 1. 获取购物车信息
     * 2. 创建订单
     * 3. 清购物车
     * 4. 修改商品信息
     * 5. 生成签名信息
     * 6. 创建预付订单
     * 7. 返回给前端
     * 1. 预订单
     * 2. 随机字符串
     * 3. 时间戳
     * 4. 签名信息
     *
     * @param user
     * @param addrId
     */
    @Override
    public Order order(User user, Integer addrId) {
        //获取购物车
        List<CartItem> cart = getMyCart(user);
        //创建订单对象
        Order order = new Order();
        long orderId = snowFlake.nextId();
        order.setOrderId(orderId);
        order.setCreateTime(new Date());
        order.setState(0);
        order.setAddressId(addrId);
        order.setUserId(user.getUserId());
        //总价
        double total = 0;
        for (CartItem cartItem : cart) {
            total += cartItem.getPrice();
        }
        order.setOrderPrice(total);
        //插入数据库中
        //orderMapper.insert(order);
        List<OrderItem> items = new ArrayList<>();
        //订单项
        for (CartItem cartItem : cart) {
            OrderItem orderItem = new OrderItem(cartItem);
            orderItem.setItemId(snowFlake.nextId());
            orderItem.setOrderId(orderId);
            //把所有的orderItems存入数据库中
            //orderItemMapper.insert(orderItem);
            //更新图书信息,销量和库存
            //Long bookId = cartItem.getBook().getBookId();
            //读库存，判断： 提高事务的隔离级别
            //更新图书的销量和库存
            //bookService.updateStockAndSalesAmount(bookId, cartItem.getCount());
            items.add(orderItem);
        }
        order.setOrderItems(items);
        //存入mq
        rabbitTemplate.convertAndSend("orderExchange", "orderKey", order);
        rabbitTemplate.convertAndSend("cancelExchange", "cancelKey", order);
        //清空购物车
        redisUtil.delKey("cart::" + user.getUserId());
        return order;
    }

    public Map<String, Object> createPayOrder(Order order, User user) {
        List<OrderItem> orderItems = order.getOrderItems();
        //购物车中第一本书的名字
        String name = orderItems.get(0).getBook().getBookName();
        if (orderItems.size() > 1) {
            name += "等商品";
        }
        //创建一个支付订单：1. 预订单  2. 随机字符串  3. 时间戳   4. 签名信息
        Map<String, Object> orderInfo = wxUtil.createOrder(order.getOrderId(), name, (int) (order.getOrderPrice() * 100), user.getOpenid());
        orderInfo.put("paySign", wxUtil.getPaySign(orderInfo));
        //设置从现在开始，30分钟后如果未支付就把订单取消，释放库存
        return orderInfo;
    }

    @Override
    public void cancelIfNotPa(Order order) {
        int i = orderMapper.cancelIfNotPaid(order.getOrderId());
        if (i != 1) {
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem item : orderItems) {
                bookMapper.updateStockAndSalesAmount(item.getBookId(), item.getCount() * (-1));
            }
        }
    }

    @Override
    public User getByWxId(String wxId) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("wx_id", wxId));
    }

    @Override
    public void dealPayResult(PayResultVO payResultVO) {
        //取出密文
        String ciphertext = payResultVO.getResource().getCiphertext();
        String nonce = payResultVO.getResource().getNonce();
        String associated_data = payResultVO.getResource().getAssociated_data();
        //解密
        String s = wxUtil.decryptToString(associated_data.getBytes(), nonce.getBytes(), ciphertext);
        try {
            Map map = JsonMapper.builder().build().readValue(s, Map.class);
            String out_trade_no = (String) map.get("out_trade_no");
            String trade_state_desc = (String) map.get("trade_state_desc");
            //支付成功
            if (trade_state_desc != null && trade_state_desc.equals("支付成功")) {
                //修改订单状态
                paySuccess(out_trade_no);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        return orderMapper.getByUserId(userId);
    }

    private void paySuccess(String out_trade_no) {
        //0 未支付  1 已支付  2 支付超时  3 订单取消  4 删除
        orderMapper.updateState(out_trade_no, 1);
    }
}
