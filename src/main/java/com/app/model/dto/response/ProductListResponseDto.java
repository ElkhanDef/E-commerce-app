package com.app.model.dto.response;

import java.math.BigDecimal;

public class ProductListResponseDto {

    private String id;
    private String name;
    private BigDecimal price;
    private String slug;

    public ProductListResponseDto(String id, String slug,
                                  BigDecimal price, String name) {
        this.id = id;
        this.slug = slug;
        this.price = price;
        this.name = name;
    }

    public ProductListResponseDto() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public static Builder builder() {
        return new Builder();
    }

    @SuppressWarnings("checkstyle:HiddenField")
    public static class Builder {

        private String id;
        private String name;
        private BigDecimal price;
        private String slug;

        public Builder id(String id) {
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

        public ProductListResponseDto build() {
            return new ProductListResponseDto(id, slug, price, name);
        }
    }
}
