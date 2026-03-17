package com.app.controller;

import com.app.model.dto.request.SignUpRequestDto;
import com.app.model.dto.response.SignUpResponseDto;
import com.app.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
