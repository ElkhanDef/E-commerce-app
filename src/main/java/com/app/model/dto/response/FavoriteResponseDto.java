package com.app.model.dto.response;

public class FavoriteResponseDto {

    private Long id;
    private FavoriteProductDto product;
    private String message;

    public FavoriteResponseDto() {}

    public FavoriteResponseDto(Long id, FavoriteProductDto product, String message) {
        this.id = id;
        this.product = product;
        this.message = message;
    }

    public static Builder builder() {
        return new Builder();
    }

    @SuppressWarnings("checkstyle:HiddenField")
    public static class Builder {
        private Long id;
        private FavoriteProductDto product;
        private String message;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder product(FavoriteProductDto product) {
            this.product = product;
            return this;
        }
        public Builder message(String message) {
            this.message = message;
            return this;
        }
        public FavoriteResponseDto build() {
            return new FavoriteResponseDto(id, product, message);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FavoriteProductDto getProduct() {
        return product;
    }

    public void setProduct(FavoriteProductDto product) {
        this.product = product;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
