package com.bookstore.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bookstore.common.entity.Book;
import com.bookstore.admin.mapper.BookMapper;
import com.bookstore.admin.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookMapper bookMapper;

    @Override
    @Cacheable(value = "books")
    public List<Book> getAll() {
        return bookMapper.selectList(new QueryWrapper<>());
    }

    @Override
    public Book getByBookId(Long bookId) {
        return bookMapper.selectById(bookId);
    }

    @Override
    public void add(Book book) {
        bookMapper.insert(book);
    }

    @Override
    public void updateStockAndSalesAmount(Long bookId, int count) {
        bookMapper.updateStockAndSalesAmount(bookId, count);
    }
}
