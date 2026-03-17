package com.app.service.impl;

import com.app.exception.ApplicationException;
import com.app.exception.data.ErrorCode;
import com.app.model.dto.request.SignUpRequestDto;
import com.app.model.dto.response.SignUpResponseDto;
import com.app.model.entity.UserEntity;
import com.app.repository.UserRepository;
import com.app.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public SignUpResponseDto signUp(SignUpRequestDto requestDto) {
        log.info("ActionLog.signUp.start");
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            log.warn("ActionLog.signUp.emailExists email: {}", requestDto.getEmail());
            throw new ApplicationException(ErrorCode.USER_ALREADY_EXISTS);
        }
        UserEntity user = new UserEntity();
        user.setName(requestDto.getName());
        user.setLastName(requestDto.getLastName());  // FIXME: CHANGE WITH MAPPER
        user.setEmail(requestDto.getEmail());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setPhoneNumber(requestDto.getPhoneNumber());
        user.setVerified(true); //FIXME: SEND EMAIL FOR VERIFY
        UserEntity savedUser = userRepository.save(user);
        log.info("ActionLog.signUp.end");

        return SignUpResponseDto.builder()
                .name(savedUser.getName())
                .lastName(savedUser.getLastName())
                .email(savedUser.getEmail())
                .phoneNumber(savedUser.getPhoneNumber())
                .build();
    }
}
