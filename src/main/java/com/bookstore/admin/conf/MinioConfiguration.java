package com.bookstore.admin.conf;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfiguration {
    @Value("${minio.ak}")
    private String ak;

    @Value("${minio.sk}")
    private String sk;

    @Value("${minio.ep}")
    private String ep;

    @Bean
    public MinioClient minioClient() {
        MinioClient minioClient = MinioClient.builder().endpoint(ep).credentials(ak, sk).build();
        return minioClient;
    }
}
