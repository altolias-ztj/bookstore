package com.bookstore.common.entity.wx;

import lombok.Data;

@Data
public class TelNumber {
    private int errCode;
    private String errMsg;
    private PhoneInfo phone_info;
    @Data
    public static class PhoneInfo{
        private String phoneNumber;
        private String purePhoneNumber;
        private String countryCode;
        private WaterMark waterMark;
}
    @Data
    public static class WaterMark{
        private int timeStamp;
        private String appid;
    }
}

