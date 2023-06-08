package com.bookstore.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bookstore.service.UserService;
import com.bookstore.entity.User;
import com.bookstore.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public User login(String name, String password) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("user_name", name).eq("user_password", password));
    }

    @Override
    public User getByUserId(Long id) {
        return userMapper.getByUserId(id);
    }
}
