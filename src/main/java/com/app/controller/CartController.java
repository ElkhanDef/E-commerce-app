package com.app.controller;

import com.app.exception.ApplicationException;
import com.app.exception.data.ErrorCode;
import com.app.model.dto.response.CartResponseDto;
import com.app.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping(path = "/items/{id}")
    public ResponseEntity<CartResponseDto> addToCart(@PathVariable(name = "id") Long productId,
                                                     @RequestParam int quantity,
                                                     @AuthenticationPrincipal String userId) {
        if (quantity <= 0) {
            throw new ApplicationException(ErrorCode.INVALID_QUANTITY);
        }
        return ResponseEntity.ok(cartService.addToCart(Long.valueOf(userId), productId, quantity));
    }

    @DeleteMapping(path = "/items/{id}")
    public ResponseEntity<CartResponseDto> removeFromCart(@PathVariable(name = "id") Long productId,
                                                          @AuthenticationPrincipal String userId) {
        return ResponseEntity.ok(cartService.removeFromCart(Long.valueOf(userId), productId));
    }

    @PatchMapping("items/{id}")
    public ResponseEntity<CartResponseDto> updateCartItem(@PathVariable(name = "id") Long productId,
                                                          @RequestParam int quantity,
                                                          @AuthenticationPrincipal String userId) {
        if (quantity <= 0) {
            throw new ApplicationException(ErrorCode.INVALID_QUANTITY);
        }
        return ResponseEntity.ok(cartService.updateCart(Long.valueOf(userId), productId, quantity));
    }

    @GetMapping
    public ResponseEntity<CartResponseDto> getCart(@AuthenticationPrincipal String userId) {
        return ResponseEntity.ok(cartService.getCart(Long.valueOf(userId)));
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal String userId) {
        cartService.clearCart(Long.valueOf(userId));
        return ResponseEntity.noContent().build();
    }
}
