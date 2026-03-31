package com.app.service.impl;

import com.app.config.AppProperties;
import com.app.config.JwtProperties;
import com.app.exception.ApplicationException;
import com.app.exception.data.ErrorCode;
import com.app.model.dto.request.ForgotPasswordRequestDto;
import com.app.model.dto.request.RefreshTokenRequestDto;
import com.app.model.dto.request.SignInRequestDto;
import com.app.model.dto.request.SignUpRequestDto;
import com.app.model.dto.response.RefreshTokenResponseDto;
import com.app.model.dto.response.SignInResponseDto;
import com.app.model.dto.response.SignUpResponseDto;
import com.app.model.entity.PasswordResetTokenEntity;
import com.app.model.entity.RefreshTokenEntity;
import com.app.model.entity.UserEntity;
import com.app.repository.PasswordResetTokenRepository;
import com.app.repository.RefreshTokenRepository;
import com.app.repository.UserRepository;
import com.app.security.JwtUtils;
import com.app.service.AuthService;
import com.app.service.EmailSender;
import com.app.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final JwtProperties jwtProperties;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final AppProperties appProperties;
    private final EmailSender emailSender;

    public AuthServiceImpl(UserRepository userRepository,
                           JwtUtils jwtUtils,
                           PasswordEncoder passwordEncoder,
                           JwtProperties jwtProperties,
                           RefreshTokenRepository refreshTokenRepository,
                           PasswordResetTokenRepository passwordResetTokenRepository,
                           AppProperties appProperties,
                           EmailSender emailSender) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.jwtProperties = jwtProperties;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.appProperties = appProperties;
        this.emailSender = emailSender;
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
                .id(savedUser.getId())
                .name(savedUser.getName())
                .lastName(savedUser.getLastName())
                .email(savedUser.getEmail())
                .phoneNumber(savedUser.getPhoneNumber())
                .build();
    }

    @Transactional
    public SignInResponseDto signIn(SignInRequestDto requestDto) {
        log.info("ActionLog.signIn.start");
        UserEntity user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_CREDENTIALS));

        if (!user.isActive()) {
            log.warn("ActionLog.signIn.user has been deleted: {}", user.getEmail());
            throw new ApplicationException(ErrorCode.INVALID_CREDENTIALS);
        }
        if (!user.isVerified()) {
            log.warn("ActionLog.signIn.user is not verified: {}", user.getEmail());
            throw new ApplicationException(ErrorCode.USER_NOT_VERIFIED);
        }
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            log.warn("ActionLog.signIn.User password invalid: {}", user.getEmail());
            throw new ApplicationException(ErrorCode.INVALID_CREDENTIALS);
        }

        refreshTokenRepository.revokeAllUserTokens(user.getId());
        String accessToken = jwtUtils.generateAccessToken(user);
        JwtUtils.TokenWithJti refreshTokenData = jwtUtils.generateRefreshToken(user.getId());
        saveRefreshTokenJti(refreshTokenData.jti(), user);
        log.info("ActionLog.signIn.end");

        return SignInResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .accessToken(accessToken)
                .refreshToken(refreshTokenData.token())
                .expiresIn(CommonUtils.calcTokenExpiration(jwtProperties.accessTokenExpiration()))
                .build();
    }

    @Override
    @Transactional
    public RefreshTokenResponseDto refreshToken(RefreshTokenRequestDto refreshTokenRequestDto) {
        log.info("ActionLog.refreshToken.start");
        String token = refreshTokenRequestDto.getRefreshToken();

        String jti;
        try {
            //CHECKSTYLE:OFF
            jti = jwtUtils.extractJti(token);
        } catch (Exception e) {
            log.warn("ActionLog.refreshToken.invalidToken");
            throw new ApplicationException(ErrorCode.INVALID_REFRESH_TOKEN);
        } //CHECKSTYLE:ON

        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByJtiWithLock(jti)
                .orElseThrow(() -> {
                    log.warn("ActionLog.refreshToken.jtiNotFound");
                    return new ApplicationException(ErrorCode.INVALID_CREDENTIALS);
                });

        if (refreshTokenEntity.isRevoked()) {
            log.warn("ActionLog.refreshToken.revoked - REPLAY ATTACK DETECTED! jti: {}", jti);
            refreshTokenRepository.revokeAllUserTokens(refreshTokenEntity.getUser().getId());
            throw new ApplicationException(ErrorCode.INVALID_CREDENTIALS);
        }
        if (CommonUtils.isExpired(refreshTokenEntity.getExpiryDate())){
            refreshTokenRepository.delete(refreshTokenEntity);
            throw new ApplicationException(ErrorCode.INVALID_CREDENTIALS);
        }

        UserEntity user = refreshTokenEntity.getUser();
        if (!jwtUtils.isTokenValid(token, user.getId())) {
            throw new ApplicationException(ErrorCode.INVALID_CREDENTIALS);
        }

        String newAccessToken = jwtUtils.generateAccessToken(user);
        JwtUtils.TokenWithJti newRefreshTokenData = jwtUtils.generateRefreshToken(user.getId());

        refreshTokenEntity.setRevoked(true);
        refreshTokenRepository.save(refreshTokenEntity);

        saveRefreshTokenJti(newRefreshTokenData.jti(), user);

        log.info("ActionLog.refreshToken.end");

        return RefreshTokenResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshTokenData.token())
                .expiresIn(CommonUtils.calcTokenExpiration(jwtProperties.accessTokenExpiration()))
                .build();
    }

    @Override
    @Transactional
    public void forgotPassword(ForgotPasswordRequestDto requestDto) {
        log.info("ActionLog.forgotPassword.start");
       Optional<UserEntity> userOpt =  userRepository.findByEmail(requestDto.getEmail());

       if (userOpt.isEmpty()) {
           log.warn("ActionLog.forgotPassword.end email not found");
           return;
       }
       UserEntity user = userOpt.get();
       String token = UUID.randomUUID().toString();

       passwordResetTokenRepository.invalidateResetTokens(user.getId());
        PasswordResetTokenEntity passwordResetTokenEntity = new PasswordResetTokenEntity();
        passwordResetTokenEntity.setToken(token);
        passwordResetTokenEntity.setUser(user);
        passwordResetTokenEntity.setExpiryDate(LocalDateTime.now().plusMinutes(15));
        passwordResetTokenEntity.setUsed(false);
        passwordResetTokenRepository.save(passwordResetTokenEntity);

        String resetLink = appProperties.frontendUrl() + "/reset-password?token=" + token;
        emailSender.sendPasswordResetEmail(user.getEmail(), resetLink);
        log.info("ActionLog.forgotPassword.end");
    }

    private void saveRefreshTokenJti(String jti, UserEntity user) {
        log.info("ActionLog.saveRefreshTokenJti.start");
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setJti(jti);
        refreshTokenEntity.setUser(user);
        refreshTokenEntity.setExpiryDate(
                LocalDateTime.now().plusSeconds(CommonUtils.calcTokenExpiration(jwtProperties.refreshTokenExpiration()))
        );
        refreshTokenRepository.save(refreshTokenEntity);
        log.info("ActionLog.saveRefreshTokenJti.end");
    }
}
