package com.app.repository;

import com.app.model.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    boolean existsBySlug(String slug);

    Optional<ProductEntity> findBySlug(String slug);
}
