package com.bookstore.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

@Data
@TableName("persons")
public class Person {
    @TableId
    private int id;
    private String name;
    private List<String> hobby;
}
