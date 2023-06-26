package com.bookstore.admin.web;

import com.bookstore.common.entity.Permission;
import com.bookstore.admin.service.PermissionService;
import com.bookstore.common.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping("getAll")
    public Object getAll() {
        return new ResponseResult(200, "", permissionService.getAll());
    }

    @PostMapping("add")
    public Object add(@RequestBody Permission permission) {
        permissionService.add(permission);
        return new ResponseResult(200, "", null);
    }
}
