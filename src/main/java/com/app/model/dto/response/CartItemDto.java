package com.app.model.dto.response;

import java.math.BigDecimal;

public class CartItemDto {

    private Long productId;
    private String productName;
    private BigDecimal unitPrice;
    private int quantity;
    private String mainImageUrl;

    public CartItemDto(Long productId, String productName,
                       BigDecimal unitPrice, int quantity,
                       String mainImageUrl) {
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.mainImageUrl = mainImageUrl;
    }

    public CartItemDto() {}

    public BigDecimal getTotalPrice() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public static Builder builder() {
        return new Builder();
    }

    @SuppressWarnings("checkstyle:HiddenField")
    public static class Builder {
        private Long productId;
        private String productName;
        private BigDecimal unitPrice;
        private int quantity;
        private String mainImageUrl;

        public Builder productId(Long productId) {
            this.productId = productId;
            return this;
        }
        public Builder productName(String productName) {
            this.productName = productName;
            return this;
        }
        public Builder unitPrice(BigDecimal unitPrice) {
            this.unitPrice = unitPrice;
            return this;
        }
        public Builder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }
        public Builder mainImageUrl(String mainImageUrl) {
            this.mainImageUrl = mainImageUrl;
            return this;
        }
        public CartItemDto build() {
            return new CartItemDto(productId, productName, unitPrice, quantity, mainImageUrl);
        }
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getMainImageUrl() {
        return mainImageUrl;
    }

    public void setMainImageUrl(String mainImageUrl) {
        this.mainImageUrl = mainImageUrl;
    }
}
