package com.bookstore.admin.web;

import com.bookstore.common.entity.Md5Data;
import com.bookstore.common.util.MinioUtil;
import com.bookstore.common.util.QiniuUtil;
import com.bookstore.common.util.ResponseResult;
import com.bookstore.common.entity.Book;
import com.bookstore.admin.service.BookService;
import io.minio.GetObjectResponse;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private QiniuUtil qiniuUtil;

    @Autowired
    private MinioUtil minioUtil;

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
        bookService.add(book);
        return new ResponseResult(200, "", null);
    }

    @PreAuthorize("hasAnyAuthority('book:update')")
    @PostMapping("update")
    @CacheEvict(value = "book", key = "#book.bookId")
    public Object update(@RequestBody @Valid Book book, BindingResult result) {
        return new ResponseResult(200, "", null);
    }

    @GetMapping("uploadToken")
    public Object uploadToken() {
        return new ResponseResult(200, "", qiniuUtil.getUploadToken());
    }

    @GetMapping("/showImg")
    public void showImg(String fileName, HttpServletResponse response) throws IOException {
        GetObjectResponse resp = minioUtil.getObject(fileName);
        String contentType = resp.headers().get("Content-Type");
        response.setContentType(contentType);
        ServletOutputStream os = response.getOutputStream();
        IOUtils.copy(resp, os);
    }

    @PostMapping("/uploadImg")
    public Object uploadImg(MultipartFile file) {
        String filename = minioUtil.putObject(file);
        return new ResponseResult(200, "", filename);
    }

    @PostMapping("checkMd5")
    public Object checkMd5(@RequestBody Md5Data data) {
        List<Book> books = bookService.getAll();
        for (Book book : books) {
            if (book.getBookCover().equals(data.getMd5())) {
                return new ResponseResult(301, "", null);
            }
        }
        return new ResponseResult(302, "", null);
    }
}
