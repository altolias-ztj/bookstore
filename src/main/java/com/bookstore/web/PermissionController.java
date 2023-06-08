package com.bookstore.web;

import com.bookstore.entity.Permission;
import com.bookstore.service.PermissionService;
import com.bookstore.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping("getAll")
    public Object getAll() {
        return new ResponseResult(200, "", permissionService.list());
    }

    @PostMapping("add")
    public Object add(@RequestBody Permission permission) {
        if (permissionService.save(permission)) {
            return new ResponseResult(200, "", null);
        } else {
            return new ResponseResult(405, "error", null);
        }
    }
}
