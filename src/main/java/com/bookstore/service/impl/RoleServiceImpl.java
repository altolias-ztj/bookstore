package com.bookstore.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bookstore.entity.Role;
import com.bookstore.mapper.RoleMapper;
import com.bookstore.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public void add(int roleId, int permissionId) {
        roleMapper.add(roleId, permissionId);
    }

    @Override
    public void del(int roleId, int permissionId) {
        roleMapper.del(roleId, permissionId);
    }
}
