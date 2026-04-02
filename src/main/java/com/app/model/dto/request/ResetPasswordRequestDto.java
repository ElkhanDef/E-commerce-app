package com.app.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ResetPasswordRequestDto {

    @NotBlank(message = "Token alanı boş olamaz")
    private String token;

    @NotBlank(message = "Şifre alanı boş olamaz")
    @Size(min = 8, message = "Şifre en az 8 karakter olmalıdır")
    private String newPassword;

    public ResetPasswordRequestDto() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
