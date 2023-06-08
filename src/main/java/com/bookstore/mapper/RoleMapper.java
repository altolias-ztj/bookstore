package com.bookstore.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bookstore.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    Role getByRoleId(int id);

    void add(@Param("rid") int roleId,@Param("pid") int permissionId);

    void del(@Param("rid") int roleId,@Param("pid") int permissionId);
}
