package com.bookstore.common.util;

import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class QiniuUtil {

    @Value("${woniu.qiniu.AccessKey}")
    private String accessKey;

    @Value("${woniu.qiniu.SecretKey}")
    private String secretKey;

    @Value("${woniu.qiniu.Bucket}")
    private String bucket;

    public String getUploadToken() {
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        return upToken;
    }
}
