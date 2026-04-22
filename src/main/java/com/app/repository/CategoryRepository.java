package com.app.repository;

import com.app.model.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity,Long> {

    boolean existsBySlug(String slug);

    Optional<CategoryEntity> findBySlug(String slug);
}
