package com.bookstore.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bookstore.entity.Role;

public interface RoleService extends IService<Role> {
    void add(int roleId, int permissionId);

    void del(int roleId, int permissionId);
}
