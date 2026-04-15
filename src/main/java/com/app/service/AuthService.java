package com.app.service;

import com.app.model.dto.request.ForgotPasswordRequestDto;
import com.app.model.dto.request.RefreshTokenRequestDto;
import com.app.model.dto.request.ResetPasswordRequestDto;
import com.app.model.dto.request.SignInRequestDto;
import com.app.model.dto.request.SignUpRequestDto;
import com.app.model.dto.response.RefreshTokenResponseDto;
import com.app.model.dto.response.SignInResponseDto;
import com.app.model.dto.response.SignUpResponseDto;
import com.app.model.dto.response.TokenVerifyResponseDto;

public interface AuthService {

    SignUpResponseDto signUp(SignUpRequestDto requestDto);
    SignInResponseDto signIn(SignInRequestDto requestDto);
    RefreshTokenResponseDto refreshToken(RefreshTokenRequestDto refreshTokenRequestDto);
    void forgotPassword(ForgotPasswordRequestDto requestDto);
    TokenVerifyResponseDto verifyReset(String token);
    void resetPassword(ResetPasswordRequestDto requestDto);
    void verifyAccount(String token);
    void signOut();
}
