package com.app.repository;

import com.app.model.entity.PasswordResetTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokenEntity, Long> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE PasswordResetTokenEntity prt SET prt.isUsed = true WHERE prt.user.id = :userId AND prt.isUsed = false")
    void invalidateResetTokens(Long userId);
}
