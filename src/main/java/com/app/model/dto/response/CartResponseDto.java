package com.app.model.dto.response;

import java.math.BigDecimal;
import java.util.List;

public class CartResponseDto {

    private Long userId;
    private String message;
    private int totalQuantity;
    private BigDecimal totalPrice;
    private List<CartItemDto> cartItems;

    public CartResponseDto(Long userId, String message, int totalQuantity,
                           BigDecimal totalPrice, List<CartItemDto> cartItems) {
        this.userId = userId;
        this.message = message;
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;
        this.cartItems = cartItems;
    }

    public CartResponseDto() {}

    public static Builder builder() {
        return new Builder();
    }

    @SuppressWarnings("checkstyle:HiddenField")
    public static class Builder {
        private Long userId;
        private String message;
        private int totalQuantity;
        private BigDecimal totalPrice;
        private List<CartItemDto> cartItems;

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }
        public Builder message(String message) {
            this.message = message;
            return this;
        }
        public Builder totalQuantity(int totalQuantity) {
            this.totalQuantity = totalQuantity;
            return this;
        }
        public Builder totalPrice(BigDecimal totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }
        public Builder cartItems(List<CartItemDto> cartItems) {
            this.cartItems = cartItems;
            return this;
        }
        public CartResponseDto build() {
            return new CartResponseDto(userId, message, totalQuantity, totalPrice, cartItems);
        }
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<CartItemDto> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItemDto> cartItems) {
        this.cartItems = cartItems;
    }
}
