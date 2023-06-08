package com.bookstore.web;

import com.bookstore.interceptor.LoginInterceptor;
import com.bookstore.service.UserService;
import com.bookstore.util.JwtUtil;
import com.bookstore.util.ResponseResult;
import com.bookstore.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private JwtUtil jwtUtil;

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
    @Cacheable(value = "user",key = "#user.userId")
    public Object getMyInfo(@SessionAttribute User user) {
        return new ResponseResult(200, "", user);
    }
}
