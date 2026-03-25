package com.app.utils;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class CommonUtils {

    public long calcTokenExpiration(long time) {
        return time / 1000;
    }

    public boolean isExpired(LocalDateTime expiryDate) {
        return LocalDateTime.now().isAfter(expiryDate);
    }
}
