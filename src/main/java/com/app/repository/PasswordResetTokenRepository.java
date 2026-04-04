package com.app.repository;

import com.app.model.entity.PasswordResetTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokenEntity, Long> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE PasswordResetTokenEntity prt SET prt.isUsed = true WHERE prt.user.id = :userId AND prt.isUsed = false")
    void invalidateResetTokens(Long userId);

    Optional<PasswordResetTokenEntity> findByToken(String token);

    @Query("SELECT prt FROM PasswordResetTokenEntity prt JOIN FETCH prt.user WHERE prt.token = :token")
    Optional<PasswordResetTokenEntity> findByTokenWithUser(String token);

    @Modifying
    @Query("DELETE FROM PasswordResetTokenEntity prt WHERE prt.expiryDate < :date")
    void deleteAllExpiredTokens(LocalDateTime date);
}
