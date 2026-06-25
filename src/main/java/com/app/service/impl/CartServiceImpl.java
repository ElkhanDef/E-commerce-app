package com.app.service.impl;

import com.app.config.FileStorageProperties;
import com.app.exception.ApplicationException;
import com.app.exception.data.ErrorCode;
import com.app.model.dto.response.CartItemDto;
import com.app.model.dto.response.CartResponseDto;
import com.app.model.entity.ProductEntity;
import com.app.repository.ProductImageRepository;
import com.app.repository.ProductRepository;
import com.app.service.CartService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CartServiceImpl implements CartService {

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final FileStorageProperties fileStorageProperties;
    private final Cache<Long, List<CartItemDto>> cartCache =
            Caffeine.newBuilder()
                    .expireAfterWrite(Duration.ofDays(7))
                    .maximumSize(1000)
                    .build();

    public CartServiceImpl(ProductRepository productRepository, ProductImageRepository productImageRepository,
                           FileStorageProperties fileStorageProperties) {
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
        this.fileStorageProperties = fileStorageProperties;
    }


    @Override
    public CartResponseDto addToCart(Long userId, Long productId, int quantity) {
        log.info("ActionLog.addToCart.start");
        List<CartItemDto> cartItems = cartCache.get(userId, k -> new ArrayList<>());

        Optional<CartItemDto> existingItem = cartItems.stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItemDto item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {

            ProductEntity product = productRepository.findById(productId)
                    .orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND));

            String mainImagePath = productImageRepository.findMainImagePath(productId)
                    .orElseThrow(() -> new ApplicationException(ErrorCode.MAIN_IMAGE_NOT_FOUND));

            String baseUrl = fileStorageProperties.endpoint() + "/" + fileStorageProperties.bucket() + "/";
            String fullUrl = baseUrl + mainImagePath;

            cartItems.add(CartItemDto.builder()
                    .productId(productId)
                    .quantity(quantity)
                    .productName(product.getName())
                    .mainImageUrl(fullUrl)
                    .unitPrice(product.getPrice())
                    .build());
        }
        log.info("ActionLog.addToCart.end");
        return CartResponseDto.builder()
                .cartItems(cartItems)
                .message("Cart successfully updated")
                .userId(userId)
                .totalPrice(calculateTotalPrice(cartItems))
                .totalQuantity(calculateTotalQuantity(cartItems))
                .build();
    }

    @Override
    public CartResponseDto removeFromCart(Long userId, Long productId) {
        log.info("ActionLog.removeFromCart.start");
        List<CartItemDto> cartItems = cartCache.getIfPresent(userId);
        if (cartItems == null) {
            throw new ApplicationException(ErrorCode.CART_EMPTY);
        }
        boolean removed = cartItems.removeIf(item -> productId.equals(item.getProductId()));
        if (!removed) {
            throw new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND_IN_CART);
        }
        log.info("ActionLog.removeFromCart.end");
        return CartResponseDto.builder()
                .cartItems(cartItems)
                .message("Cart successfully updated")
                .userId(userId)
                .totalPrice(calculateTotalPrice(cartItems))
                .totalQuantity(calculateTotalQuantity(cartItems))
                .build();
    }

    @Override
    public CartResponseDto getCart(Long userId) {
        log.info("ActionLog.getCart.start");
        List<CartItemDto> cartItems = cartCache.getIfPresent(userId);
        if (cartItems == null) {
            throw new ApplicationException(ErrorCode.CART_EMPTY);
        }
        log.info("ActionLog.getCart.end");
        return CartResponseDto.builder()
                .cartItems(cartItems)
                .message("Cart retrieved successfully")
                .userId(userId)
                .totalPrice(calculateTotalPrice(cartItems))
                .totalQuantity(calculateTotalQuantity(cartItems))
                .build();
    }

    @Override
    public CartResponseDto updateCart(Long userId, Long productId, int quantity) {
        log.info("ActionLog.updateCart.start");
        List<CartItemDto> cartItems = cartCache.getIfPresent(userId);
        if (cartItems == null) {
            throw new ApplicationException(ErrorCode.CART_EMPTY);
        }
        CartItemDto item = cartItems.stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND_IN_CART));

        if (quantity <= 0) {
            cartItems.remove(item);
        } else {
            item.setQuantity(quantity);
        }
        log.info("ActionLog.updateCart.end");
        return CartResponseDto.builder()
                .cartItems(cartItems)
                .message("Cart updated successfully")
                .userId(userId)
                .totalPrice(calculateTotalPrice(cartItems))
                .totalQuantity(calculateTotalQuantity(cartItems))
                .build();
    }

    @Override
    public void clearCart(Long userId) {
        log.info("ActionLog.clearCart.start");
        List<CartItemDto> cartItems = cartCache.getIfPresent(userId);
        if (cartItems == null) {
            throw new ApplicationException(ErrorCode.CART_EMPTY);
        }
        cartCache.invalidate(userId);
        log.info("ActionLog.clearCart.end");
    }

    private int calculateTotalQuantity(List<CartItemDto> cartItems) {
        return cartItems.stream().mapToInt(CartItemDto::getQuantity).sum();
    }
    private BigDecimal calculateTotalPrice(List<CartItemDto> cartItems) {
        return cartItems.stream().map(CartItemDto::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
