package com.app.service;

import com.app.model.dto.response.CartResponseDto;

public interface CartService {

    CartResponseDto addToCart(Long userId, Long productId, int quantity);

    CartResponseDto removeFromCart(Long userId, Long productId);

    CartResponseDto getCart(Long userId);

    CartResponseDto updateCart(Long userId, Long productId, int quantity);

    void clearCart(Long userId);
}
