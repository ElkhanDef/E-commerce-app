package com.app.service;

import com.app.model.dto.request.RefreshTokenRequestDto;
import com.app.model.dto.request.SignInRequestDto;
import com.app.model.dto.request.SignUpRequestDto;
import com.app.model.dto.response.RefreshTokenResponseDto;
import com.app.model.dto.response.SignInResponseDto;
import com.app.model.dto.response.SignUpResponseDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthService {

    SignUpResponseDto signUp(SignUpRequestDto requestDto);
    SignInResponseDto signIn(SignInRequestDto requestDto);
    RefreshTokenResponseDto refreshToken(RefreshTokenRequestDto refreshTokenRequestDto);
}
