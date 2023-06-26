package com.bookstore.admin;

import com.bookstore.admin.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BookTests {

    @Autowired
    private BookService bookService;

    @Test
    public void contextLoads() {
        System.out.println(bookService.list());
    }

    @Test
    public void getRole(){

    }
}
