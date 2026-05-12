package com.app.model.dto.response;

import java.math.BigDecimal;
import java.util.List;

public class ProductResponseDto {

    private Long id;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private String sku;
    private String description;
    private String slug;
    private CategoryResponseDto categoryResponse;
    private List<ImageResponseDto> imagesResponse;

    public ProductResponseDto(Long id,
                              String name,
                              BigDecimal price,
                              Integer stock,
                              String sku,
                              String description,
                              String slug,
                              CategoryResponseDto categoryResponse,
                              List<ImageResponseDto> imagesResponse) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.sku = sku;
        this.description = description;
        this.slug = slug;
        this.categoryResponse = categoryResponse;
        this.imagesResponse = imagesResponse;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public BigDecimal getPrice() {return price;}
    public void setPrice(BigDecimal price) {this.price = price;}
    public Integer getStock() {return stock;}
    public void setStock(Integer stock) {this.stock = stock;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
    public String getSku() {return sku;}
    public void setSku(String sku) {this.sku = sku;}
    public String getSlug() {return slug;}
    public void setSlug(String slug) {this.slug = slug;}
    public CategoryResponseDto getCategoryResponse() {return categoryResponse;}
    public void setCategoryResponse(CategoryResponseDto categoryResponse) {this.categoryResponse = categoryResponse;}
    public List<ImageResponseDto> getImagesResponse() {return imagesResponse;}
    public void setImagesResponse(List<ImageResponseDto> imagesResponse) {this.imagesResponse = imagesResponse;}

    public static Builder builder() {
        return new Builder();
    }

    @SuppressWarnings("checkstyle:HiddenField")
    public static class Builder {
        private Long id;
        private String name;
        private BigDecimal price;
        private Integer stock;
        private String sku;
        private String description;
        private String slug;
        private CategoryResponseDto categoryResponse;
        private List<ImageResponseDto> imagesResponse;

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

        public Builder stock(Integer stock) {
            this.stock = stock;
            return this;
        }

        public Builder sku(String sku) {
            this.sku = sku;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder slug(String slug) {
            this.slug = slug;
            return this;
        }

        public Builder categoryResponse(CategoryResponseDto categoryResponse) {
            this.categoryResponse = categoryResponse;
            return this;
        }

        public Builder imagesResponse(List<ImageResponseDto> imagesResponse) {
            this.imagesResponse = imagesResponse;
            return this;
        }

        public ProductResponseDto build() {
            return new ProductResponseDto(
                    id,
                    name,
                    price,
                    stock,
                    sku,
                    description,
                    slug,
                    categoryResponse,
                    imagesResponse
            );
        }
    }
}
