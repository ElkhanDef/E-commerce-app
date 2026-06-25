package com.app.repository;

import com.app.model.entity.ProductImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImageEntity, Long> {

    @Modifying
    @Query("UPDATE ProductImageEntity pi SET pi.isMain = false WHERE pi.product.id = :productId")
    void unsetAllMainImages(Long productId);

    @Modifying
    @Query("UPDATE ProductImageEntity pi SET pi.isMain = true WHERE pi.product.id = :productId AND pi.id = :imageId")
    int setMainImage(Long productId, Long imageId);

    @Query("SELECT p.imagePath FROM ProductImageEntity p WHERE p.isMain = true AND p.product.id = :productId")
    Optional<String> findMainImagePath(@Param("productId") Long productId);
}
