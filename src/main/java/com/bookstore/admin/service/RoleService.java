package com.bookstore.admin.service;


import com.bookstore.common.entity.Role;

import java.util.List;

public interface RoleService{
    void add(int roleId, int permissionId);

    void del(int roleId, int permissionId);

    List<Role> getAll();
}
