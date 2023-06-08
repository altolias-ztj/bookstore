package com.bookstore.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bookstore.entity.User;

public interface UserService extends IService<User> {
    public User login(String name,String password);

    public User getByUserId(Long id);
}
