package com.bookstore.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Past;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("books")
public class Book implements Serializable {
    @TableId(type = IdType.ASSIGN_ID)
    private Long bookId;
    @Length(min = 1)
    private String bookName;
    private double bookPrice;
    private String bookPublish;
    private String bookAuthor;
    private String bookIsbn;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Past
    private Date bookPublishTime;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date bookCreateTime;
    private int bookState;
    private int bookStock;
    private String bookCover;
}
