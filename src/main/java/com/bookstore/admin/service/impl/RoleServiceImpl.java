package com.bookstore.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bookstore.admin.mapper.RoleMapper;
import com.bookstore.admin.service.RoleService;
import com.bookstore.common.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

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

    @Override
    public List<Role> getAll() {
        return roleMapper.selectList(new QueryWrapper<Role>());
    }
}
