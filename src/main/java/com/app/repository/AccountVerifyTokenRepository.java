package com.app.repository;

import com.app.model.entity.AccountVerifyTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface AccountVerifyTokenRepository extends JpaRepository<AccountVerifyTokenEntity, Long> {

    Optional<AccountVerifyTokenEntity> findByToken(String token);

    @Modifying
    @Query("DELETE FROM AccountVerifyTokenEntity avt WHERE avt.expiryDate < :date")
    void deleteAllExpiredTokens(LocalDateTime date);
}
