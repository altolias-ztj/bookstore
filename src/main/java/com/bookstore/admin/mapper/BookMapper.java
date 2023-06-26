package com.bookstore.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bookstore.common.entity.Book;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BookMapper extends BaseMapper<Book> {
    void updateStockAndSalesAmount(Long bookId, int count);

    Book getByBookId(Long bookId);
}
