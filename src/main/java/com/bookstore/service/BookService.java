package com.bookstore.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bookstore.entity.Book;

import java.util.List;

public interface BookService extends IService<Book> {
    List<Book> getAll();
}
