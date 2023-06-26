package com.bookstore.common.entity.wx;

import lombok.Data;

@Data
public class Token {
    private String access_token ;
    private int expires_in;
}
