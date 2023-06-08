package com.bookstore.entity;

import lombok.Data;

@Data
public class RolePermissionVO {
    private int RoleId;
    private int permissionId;
    private boolean checked;
}
