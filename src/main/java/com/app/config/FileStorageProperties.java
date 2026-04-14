package com.app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "minio")
public record FileStorageProperties(
        String endpoint,
        String accessKey,
        String secretKey
) {
}
