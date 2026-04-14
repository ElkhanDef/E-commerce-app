package com.app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.servlet.multipart")
public record FileProperties(
        long maxFileSize,
        long maxRequestSize
) {
}
