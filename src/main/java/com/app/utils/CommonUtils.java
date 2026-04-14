package com.app.utils;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.UUID;

@UtilityClass
public class CommonUtils {

    public long calcTokenExpiration(long time) {
        return time / 1000;
    }

    public boolean isExpired(LocalDateTime expiryDate) {
        return LocalDateTime.now().isAfter(expiryDate);
    }

    public String generateUniqueImageName(String originalImageName) {
        if (originalImageName == null) {
            return UUID.randomUUID().toString().replace("-", "") + ".jpg";
        }
        String extension = originalImageName.substring(originalImageName.lastIndexOf("."));
        return UUID.randomUUID().toString().replace("-", "") + extension;
    }
}
