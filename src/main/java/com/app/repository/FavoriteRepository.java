package com.app.repository;

import com.app.model.entity.FavoriteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Long> {

    @Query("SELECT f FROM FavoriteEntity f WHERE f.user.id = :userId AND f.product.id = :productId")
    Optional<FavoriteEntity> findByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);
}
