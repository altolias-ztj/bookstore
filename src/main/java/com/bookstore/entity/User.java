package com.bookstore.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
}
