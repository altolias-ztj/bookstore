package com.bookstore.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("addrs")
public class Addr {
    @TableId
    private int addrId;
    private String address;
    private String tel;
    private String name;
}
