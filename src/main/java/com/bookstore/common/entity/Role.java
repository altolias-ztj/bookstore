package com.bookstore.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bookstore.common.entity.Permission;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@TableName("roles")
public class Role implements Serializable {
    private int roleId;
    private String roleName;
    @TableField(exist = false)
    List<Permission> permissions = new ArrayList<>();
}
