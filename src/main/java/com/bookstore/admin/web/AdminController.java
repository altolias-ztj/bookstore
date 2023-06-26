package com.bookstore.admin.web;

import com.bookstore.admin.service.UserService;
import com.bookstore.common.util.JwtUtil;
import com.bookstore.common.util.RedisUtil;
import com.bookstore.common.util.ResponseResult;
import com.bookstore.common.entity.User;
import com.bookstore.common.util.WxUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class AdminController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private WxUtil wxUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserService userService;

    @PostMapping("login")
    public Object login(@RequestBody User user) {
        User u = userService.login(user.getUserName(), user.getUserPassword());
        if (u == null) {
            return new ResponseResult(404, "name or password error", null);
        } else {
            return new ResponseResult(200, "", jwtUtil.getToken(u));
        }
    }

    @GetMapping("getMyInfo")
    public Object getMyInfo(@SessionAttribute User user) {
        return new ResponseResult(200, "", user);
    }

    @GetMapping("/ticket")
    public Object ticket() {
        String ticket = wxUtil.getTicket();
        return new ResponseResult(200, "", ticket);
    }

    @GetMapping("/scanned")
    public Object scanned(String ticket) {
        User user = redisUtil.get(ticket, User.class);
        if (user != null) {
            String userToken = jwtUtil.getToken(user);
            return new ResponseResult(200, "", userToken);
        }
        return new ResponseResult(201, "", null);
    }
}
