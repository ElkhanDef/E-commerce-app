package com.app.exception.data;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    //USER
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "Kullanıcı bulunamadı"),
    USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "Kullanıcı zaten mevcut"),

    //AUTH
    USER_NOT_VERIFIED(HttpStatus.UNAUTHORIZED, "Kullanıcı doğrulanmamış"),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "Geçersiz kimlik bilgileri"),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Geçersiz refresh token"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Geçersiz token"),
    TOKEN_ALREADY_USED(HttpStatus.UNAUTHORIZED, "Token zaten kullanıldı"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Token süresi doldu"),


    //VALIDATION
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "Doğrulama başarısız oldu"),
    SAME_AS_OLD_PASSWORD(HttpStatus.BAD_REQUEST, "Yeni şifreniz önceki şifrenizle aynı olamaz"),

    //CATEGORY
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "Kategori bulunamadı"),

    //PRODUCT
    IMAGE_UPLOAD_FAILED(HttpStatus.SERVICE_UNAVAILABLE, "Resim yükleme başarısız oldu"),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "Ürün bulunamadı"),

    //IMAGE VALIDATION
    FILE_EMPTY(HttpStatus.BAD_REQUEST, "Dosya boş olamaz"),
    INVALID_FILENAME(HttpStatus.BAD_REQUEST, "Dosya adı geçersiz"),
    FILE_TOO_LARGE(HttpStatus.valueOf(413), "Dosya boyutu 5MB'dan büyük olamaz"),
    INVALID_FILE_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Sadece resim dosyaları (JPEG, PNG, JPG) kabul edilir"),
    INVALID_FILE_EXTENSION(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Dosya uzantısı desteklenmiyor"),
    TOO_MANY_IMAGES(HttpStatus.BAD_REQUEST, "Çok fazla görsel yüklenemez"),
    NO_IMAGES_PROVIDED(HttpStatus.BAD_REQUEST, "Hiç görsel sağlanmadı"),


    // GENERIC
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Sunucu hatası");

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
