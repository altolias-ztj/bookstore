package com.bookstore.web;

import com.bookstore.entity.Book;
import com.bookstore.service.BookService;
import com.bookstore.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('book:all')")
    public Object all() {
        List<Book> books = bookService.getAll();
        return new ResponseResult(200, "", books);
    }

    @PostMapping("add")
    @PreAuthorize("hasAnyAuthority('book:admin')")
    public Object add(@RequestBody @Valid Book book, BindingResult result) {
        Map errors = new HashMap<>();
        List<ObjectError> allErrors = result.getAllErrors();
        if (allErrors.size() > 0) {
            for (ObjectError e : allErrors) {
                FieldError error = (FieldError) e;
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseResult(203, "", errors);
        }
        book.setBookCreateTime(new Date());
        bookService.save(book);
        return new ResponseResult(200, "", null);
    }

    @PreAuthorize("hasAnyAuthority('book:update')")
    @PostMapping("update")
    @CacheEvict(value = "book", key = "#book.bookId")
    public Object update(@RequestBody @Valid Book book, BindingResult result) {
        return new ResponseResult(200, "", null);
    }
}
