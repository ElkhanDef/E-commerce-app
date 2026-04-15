package com.app.controller;

import com.app.model.dto.request.ForgotPasswordRequestDto;
import com.app.model.dto.request.RefreshTokenRequestDto;
import com.app.model.dto.request.ResetPasswordRequestDto;
import com.app.model.dto.request.SignInRequestDto;
import com.app.model.dto.request.SignUpRequestDto;
import com.app.model.dto.response.RefreshTokenResponseDto;
import com.app.model.dto.response.SignInResponseDto;
import com.app.model.dto.response.SignUpResponseDto;
import com.app.model.dto.response.TokenVerifyResponseDto;
import com.app.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(path = "/sign-up")
    public ResponseEntity<SignUpResponseDto> signUp(@RequestBody @Valid SignUpRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signUp(requestDto));
    }

    @PostMapping(path = "/sign-up/verify")
    public ResponseEntity<Void> verifyAccount(@RequestParam(name = "token") String token) {
        authService.verifyAccount(token);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/sign-in")
    public ResponseEntity<SignInResponseDto> signIn(@RequestBody @Valid SignInRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.signIn(requestDto));
    }

    @PostMapping(path = "/refresh-token")
    public ResponseEntity<RefreshTokenResponseDto> refreshToken(@RequestBody @Valid RefreshTokenRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.refreshToken(requestDto));
    }

    @PostMapping(path = "/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestBody @Valid ForgotPasswordRequestDto requestDto) {
        authService.forgotPassword(requestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/reset-password/verify")
    public ResponseEntity<TokenVerifyResponseDto> resetPassword(@RequestParam(name = "token") String token) {
        return ResponseEntity.ok(authService.verifyReset(token));
    }

    @PostMapping(path = "/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid ResetPasswordRequestDto request) {
        authService.resetPassword(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "sign-out")
    public ResponseEntity<Void> signOut() {
        authService.signOut();
        return ResponseEntity.ok().build();
    }
}
