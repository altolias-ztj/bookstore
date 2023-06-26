package com.bookstore.user.controller;

import com.bookstore.admin.service.BookService;
import com.bookstore.common.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/book")
public class UserBookController {
    @Autowired
    private BookService bookService;

    @GetMapping("/getAll")
    @CrossOrigin
    public Object getAll() {
        return new ResponseResult(200, "", bookService.getAll());
    }
}
