package com.bookstore.common.util;

import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Component
public class MinioUtil {
    @Value("${minio.bucketName}")
    private String bucketName;

    @Autowired
    private MinioClient minioClient;

    public GetObjectResponse getObject(String filename) {
        GetObjectArgs args = GetObjectArgs.builder()
                .bucket(bucketName)
                .object(filename)
                .build();
        try {
            GetObjectResponse resp = minioClient.getObject(args);
            return resp;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String putObject(MultipartFile file) {
        String fileName = UUID.randomUUID().toString();
        String originalFilename = file.getOriginalFilename();
        String extName = originalFilename.substring(originalFilename.lastIndexOf("."));
        fileName += extName;
        try {
            InputStream is = file.getInputStream();
            long size = file.getSize();
            PutObjectArgs args = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .stream(is, size, -1)
                    .build();
            minioClient.putObject(args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return fileName;
    }
}
