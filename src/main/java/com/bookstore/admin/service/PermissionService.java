package com.bookstore.admin.service;


import com.bookstore.common.entity.Permission;

import java.util.List;

public interface PermissionService{

    List<Permission> getAll();

    void add(Permission permission);
}
