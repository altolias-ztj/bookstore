package com.bookstore.web;

import com.bookstore.entity.RolePermissionVO;
import com.bookstore.service.RoleService;
import com.bookstore.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("getAll")
    public Object getAll() {
        return new ResponseResult(200, "", roleService.list());
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
