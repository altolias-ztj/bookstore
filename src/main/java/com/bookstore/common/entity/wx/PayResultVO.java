package com.bookstore.common.entity.wx;

import lombok.Data;

@Data
public class PayResultVO {
    private String id;
    private String create_time;
    private String event_type;
    private String resource_type;
    private String summary;
    private Resource resource;

    @Data
    public static class Resource{
        //加密算法
        private String algorithm;
        //数据密文
        private String ciphertext;
        private String associated_data;
        private String original_type;
        private String nonce;
    }
}
