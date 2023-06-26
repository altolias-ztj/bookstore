package com.bookstore.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bookstore.common.entity.Role;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("users")
public class User implements Serializable {
    @TableId(type = IdType.INPUT)
    private Long userId;
    private String userName;
    private String userPassword;
    @TableField(exist = false)
    private Role role;
    private String userTel;
    @TableField(exist = false)
    private String openid;
    private String wxId;
}
