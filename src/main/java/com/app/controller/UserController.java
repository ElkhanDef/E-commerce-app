package com.app.controller;

import com.app.model.dto.response.UserResponseDto;
import com.app.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/me")
    public ResponseEntity<UserResponseDto> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }
}
