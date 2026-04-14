package com.app.service.impl;

import com.app.model.enums.SlugTarget;
import com.app.repository.CategoryRepository;
import com.app.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class SlugGeneratorService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public SlugGeneratorService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public String generateUniqueSlug(String name, SlugTarget slugTarget) {
        long count = 1;
        String baseSlug = toSlug(name);
        String uniqueSlug = baseSlug;
        while (existsBySlug(uniqueSlug, slugTarget)) {
            uniqueSlug = baseSlug + "-" + count;
            count++;
        }
        return uniqueSlug;
    }

    private String toSlug(String name) {
        String normalizedName = normalize(name);

        StringBuilder result = new StringBuilder();
        for (char c : normalizedName.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                result.append(c);
            } else {
                result.append("-");
            }
        }
        return result.toString()
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
    }

    private boolean existsBySlug(String slug, SlugTarget target) {
        return switch (target) {
            case PRODUCT -> productRepository.existsBySlug(slug);
            case CATEGORY -> categoryRepository.existsBySlug(slug);
        };
    }

    private String normalize(String input) {
        return input
                .replace("ç", "c").replace("Ç", "c")
                .replace("ş", "s").replace("Ş", "s")
                .replace("ö", "o").replace("Ö", "o")
                .replace("ü", "u").replace("Ü", "u")
                .replace("ə", "e").replace("Ə", "e")
                .replace("ı", "i").replace("İ", "i")
                .replace("ğ", "g").replace("Ğ", "g")
                .toLowerCase();
    }
}
