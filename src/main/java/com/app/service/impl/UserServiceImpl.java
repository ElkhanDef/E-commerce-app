package com.app.service.impl;

import com.app.exception.ApplicationException;
import com.app.exception.data.ErrorCode;
import com.app.mapper.UserMapper;
import com.app.model.dto.response.UserResponseDto;
import com.app.model.entity.UserEntity;
import com.app.repository.UserRepository;
import com.app.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getCurrentUser() {
        log.info("ActionLog.getCurrentUser.start");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ApplicationException(ErrorCode.USER_NOT_FOUND);
        }
        Long userId = Long.valueOf(auth.getName());

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
        log.info("ActionLog.getCurrentUser.end");
        return UserMapper.INSTANCE.toDto(user);
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        log.info("ActionLog.getUserById.start");
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
        log.info("ActionLog.getUserById.end");
        return UserMapper.INSTANCE.toDto(user);
    }
}
