package com.app.service;

import com.app.model.dto.request.SignUpRequestDto;
import com.app.model.dto.response.SignUpResponseDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthService {

    SignUpResponseDto signUp(SignUpRequestDto requestDto);
}
