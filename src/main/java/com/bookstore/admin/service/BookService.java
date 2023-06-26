package com.bookstore.admin.service;

import com.bookstore.common.entity.Book;

import java.util.List;

public interface BookService {
    List<Book> getAll();

    Book getByBookId(Long bookId);

    void add(Book book);

    void updateStockAndSalesAmount(Long bookId, int count);
}
