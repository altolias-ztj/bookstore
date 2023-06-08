package com.bookstore.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("permissions")
public class Permission implements Serializable {
    private int permissionId;
    private String permission;
    private String permissionName;
}
