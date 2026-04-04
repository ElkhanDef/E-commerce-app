package com.app.scheduler;

import com.app.repository.AccountVerifyTokenRepository;
import com.app.repository.PasswordResetTokenRepository;
import com.app.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenCleanupScheduler {

    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final AccountVerifyTokenRepository accountVerifyTokenRepository;

    @Scheduled(cron = "0 0 0 * * MON")
    @Transactional
    public void cleanupExpiredTokens() {
        log.info("ActionLog.cleanupExpiredTokens.start");
        LocalDateTime now = LocalDateTime.now();
        passwordResetTokenRepository.deleteAllExpiredTokens(now);
        refreshTokenRepository.deleteAllExpiredTokens(now);
        accountVerifyTokenRepository.deleteAllExpiredTokens(now);
        log.info("ActionLog.cleanupExpiredTokens.end");
    }
}
