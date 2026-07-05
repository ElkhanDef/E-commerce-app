package com.app.model.dto.response;

import java.math.BigDecimal;

public class FavoriteProductDto {

    private Long id;
    private String name;
    private BigDecimal price;
    private String mainImageUrl;

    public FavoriteProductDto() {}

    public FavoriteProductDto(Long id, String name, BigDecimal price, String mainImageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.mainImageUrl = mainImageUrl;
    }

    public static Builder builder() {
        return new Builder();
    }

    @SuppressWarnings("checkstyle:HiddenField")
    public static class Builder {
        private Long id;
        private String name;
        private BigDecimal price;
        private String mainImageUrl;

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
        public Builder mainImageUrl(String mainImageUrl) {
            this.mainImageUrl = mainImageUrl;
            return this;
        }
        public FavoriteProductDto build() {
            return new FavoriteProductDto(id, name, price, mainImageUrl);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getMainImageUrl() { return mainImageUrl; }
    public void setMainImageUrl(String mainImageUrl) { this.mainImageUrl = mainImageUrl; }
}
