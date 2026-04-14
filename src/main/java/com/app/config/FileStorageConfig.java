package com.app.config;

import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileStorageConfig {

    private final FileStorageProperties fileStorageProperties;

    public FileStorageConfig(FileStorageProperties fileStorageProperties) {
        this.fileStorageProperties = fileStorageProperties;
    }

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(fileStorageProperties.endpoint())
                .credentials(fileStorageProperties.accessKey(), fileStorageProperties.secretKey())
                .build();
    }
}
