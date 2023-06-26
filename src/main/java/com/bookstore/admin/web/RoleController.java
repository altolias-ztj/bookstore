package com.bookstore.admin.web;

import com.bookstore.common.util.ResponseResult;
import com.bookstore.common.entity.RolePermissionVO;
import com.bookstore.admin.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("getAll")
    public Object getAll() {
        return new ResponseResult(200, "", roleService.getAll());
    }

    @PostMapping("check")
    public Object check(@RequestBody RolePermissionVO rolePermissionVO) {
        if (rolePermissionVO.isChecked()) {
            roleService.add(rolePermissionVO.getRoleId(), rolePermissionVO.getPermissionId());
        } else {
            roleService.del(rolePermissionVO.getRoleId(), rolePermissionVO.getPermissionId());
        }
        return new ResponseResult(200, "", null);
    }
}
