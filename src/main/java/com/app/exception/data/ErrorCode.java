package com.app.exception.data;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    //USER
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "Kullanıcı bulunamadı"),
    USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST,"Kullanıcı zaten mevcut"),

    //VALIDATION
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "Doğrulama başarısız oldu");

    private final HttpStatus status;
    private final String defaultMessage;

    ErrorCode(HttpStatus status, String defaultMessage) {
        this.status = status;
        this.defaultMessage = defaultMessage;
    }

    public HttpStatus getStatus() {
        return status;
    }
    public String getDefaultMessage() {
        return defaultMessage;
    }
}
