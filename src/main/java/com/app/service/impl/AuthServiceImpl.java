package com.app.service.impl;

import com.app.config.AppProperties;
import com.app.config.JwtProperties;
import com.app.exception.ApplicationException;
import com.app.exception.data.ErrorCode;
import com.app.model.dto.request.ForgotPasswordRequestDto;
import com.app.model.dto.request.RefreshTokenRequestDto;
import com.app.model.dto.request.ResetPasswordRequestDto;
import com.app.model.dto.request.SignInRequestDto;
import com.app.model.dto.request.SignUpRequestDto;
import com.app.model.dto.response.RefreshTokenResponseDto;
import com.app.model.dto.response.SignInResponseDto;
import com.app.model.dto.response.SignUpResponseDto;
import com.app.model.dto.response.TokenVerifyResponseDto;
import com.app.model.entity.AccountVerifyTokenEntity;
import com.app.model.entity.PasswordResetTokenEntity;
import com.app.model.entity.RefreshTokenEntity;
import com.app.model.entity.UserEntity;
import com.app.repository.AccountVerifyTokenRepository;
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
    private final AccountVerifyTokenRepository accountVerifyTokenRepository;
    private final AppProperties appProperties;
    private final EmailSender emailSender;

    public AuthServiceImpl(UserRepository userRepository,
                           JwtUtils jwtUtils,
                           PasswordEncoder passwordEncoder,
                           JwtProperties jwtProperties,
                           RefreshTokenRepository refreshTokenRepository,
                           PasswordResetTokenRepository passwordResetTokenRepository,
                           AccountVerifyTokenRepository accountVerifyTokenRepository,
                           AppProperties appProperties,
                           EmailSender emailSender) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.jwtProperties = jwtProperties;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.accountVerifyTokenRepository = accountVerifyTokenRepository;
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
        user.setVerified(false);
        UserEntity savedUser = userRepository.save(user);

        String token = UUID.randomUUID().toString();

        AccountVerifyTokenEntity accountVerifyToken = new AccountVerifyTokenEntity();
        accountVerifyToken.setToken(token);
        accountVerifyToken.setExpiryDate(LocalDateTime.now().plusHours(24));
        accountVerifyToken.setUser(savedUser);
        accountVerifyToken.setUsed(false);
        accountVerifyTokenRepository.save(accountVerifyToken);

        String verifyLink = appProperties.frontendUrl() + "/verify-user/" + token;
        emailSender.sendUserVerifyEmail(user.getEmail(),verifyLink);

        log.info("ActionLog.signUp.end");

        return SignUpResponseDto.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .lastName(savedUser.getLastName())
                .email(savedUser.getEmail())
                .phoneNumber(savedUser.getPhoneNumber())
                .build();
    }

    @Override
    @Transactional
    public void verifyAccount(String token) {
        log.info("ActionLog.verifyAccount.start");
        Optional<AccountVerifyTokenEntity> tokenOpt = accountVerifyTokenRepository.findByToken(token);

        if (tokenOpt.isEmpty()){
            throw new ApplicationException(ErrorCode.INVALID_TOKEN);
        }
        AccountVerifyTokenEntity tokenEntity = tokenOpt.get();
        UserEntity user =  tokenEntity.getUser();

        if (tokenEntity.isUsed()){
            throw new ApplicationException(ErrorCode.TOKEN_ALREADY_USED);
        }
        if (tokenEntity.getExpiryDate().isBefore(LocalDateTime.now())){
            throw new ApplicationException(ErrorCode.TOKEN_EXPIRED);
        }
        user.setVerified(true);
        tokenEntity.setUsed(true);
        userRepository.save(user);
        accountVerifyTokenRepository.save(tokenEntity);
        log.info("ActionLog.verifyAccount.end");
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
        if (CommonUtils.isExpired(refreshTokenEntity.getExpiryDate())) {
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
        Optional<UserEntity> userOpt = userRepository.findByEmail(requestDto.getEmail());

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

    @Override
    public TokenVerifyResponseDto verifyReset(String token) {
        log.info("ActionLog.verifyReset.start");
        Optional<PasswordResetTokenEntity> tokenOpt = passwordResetTokenRepository.findByToken(token);

        if (tokenOpt.isEmpty()) {
            return TokenVerifyResponseDto.builder().isValid(false).build();
        }
        PasswordResetTokenEntity resetToken = tokenOpt.get();
        LocalDateTime now = LocalDateTime.now();
        boolean isOk = resetToken.getExpiryDate().isAfter(now) && !resetToken.isUsed();
        log.info("ActionLog.verifyReset.end");
        return TokenVerifyResponseDto.builder().isValid(isOk).build();
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequestDto request) {
        log.info("ActionLog.resetPassword.start");
        PasswordResetTokenEntity resetToken = passwordResetTokenRepository.findByTokenWithUser(request.getToken())
                .orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_TOKEN));

        if (resetToken.isUsed()) {
            throw new ApplicationException(ErrorCode.TOKEN_ALREADY_USED);
        }
        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new ApplicationException(ErrorCode.TOKEN_EXPIRED);
        }
        if (passwordEncoder.matches(request.getNewPassword(), resetToken.getUser().getPassword())) {
            throw new ApplicationException(ErrorCode.SAME_AS_OLD_PASSWORD);
        }

        UserEntity user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);
        log.info("ActionLog.resetPassword.end");
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
