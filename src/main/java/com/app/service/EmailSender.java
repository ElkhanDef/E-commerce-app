package com.app.service;

import com.app.model.dto.request.EmailRequestDto;

public interface EmailSender {

    void sendPasswordResetEmail(String email, String resetLink);
    void send(EmailRequestDto request);
}
