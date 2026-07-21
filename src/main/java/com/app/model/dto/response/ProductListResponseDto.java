package com.app.model.dto.response;

import java.math.BigDecimal;

public class ProductListResponseDto {

    private Long id;
    private String name;
    private BigDecimal price;
    private String slug;
    private String mainImageUrl;
    private String categorySlug;

    public ProductListResponseDto(Long id, String slug,
                                  BigDecimal price, String name,
                                  String mainImageUrl,String categorySlug) {
        this.id = id;
        this.slug = slug;
        this.price = price;
        this.name = name;
        this.mainImageUrl = mainImageUrl;
        this.categorySlug = categorySlug;
    }

    public ProductListResponseDto() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getMainImageUrl() {
        return mainImageUrl;
    }

    public void setMainImageUrl(String mainImageUrl) {
        this.mainImageUrl = mainImageUrl;
    }

    public String getCategorySlug() {
        return categorySlug;
    }
    public void setCategorySlug(String categorySlug) {
        this.categorySlug = categorySlug;
    }

    public static Builder builder() {
        return new Builder();
    }

    @SuppressWarnings("checkstyle:HiddenField")
    public static class Builder {

        private Long id;
        private String name;
        private BigDecimal price;
        private String slug;
        private String mainImageUrl;
        private String categorySlug;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }
        public Builder slug(String slug) {
            this.slug = slug;
            return this;
        }
        public Builder mainImageUrl(String mainImageUrl) {
            this.mainImageUrl = mainImageUrl;
            return this;
        }
        public Builder categorySlug(String categorySlug) {
            this.categorySlug = categorySlug;
            return this;
        }

        public ProductListResponseDto build() {
            return new ProductListResponseDto(id, slug, price, name, mainImageUrl, categorySlug);
        }
    }
}
