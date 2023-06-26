package com.bookstore.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bookstore.admin.mapper.PermissionMapper;
import com.bookstore.admin.service.PermissionService;
import com.bookstore.common.entity.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;
    @Override
    public List<Permission> getAll() {
        return permissionMapper.selectList(new QueryWrapper<Permission>());
    }

    @Override
    public void add(Permission permission) {
        permissionMapper.insert(permission);
    }
}
