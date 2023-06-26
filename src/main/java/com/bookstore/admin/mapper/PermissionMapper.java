package com.bookstore.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bookstore.common.entity.Permission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
    List<Permission> getByRoleId(int id);
}
