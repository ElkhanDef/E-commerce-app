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
import java.util.concurrent.atomic.AtomicBoolean;

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
        List<CartItemDto> currentCart = cartCache.getIfPresent(userId);
        boolean alreadyInCart = currentCart != null && currentCart.stream()
                .anyMatch(i -> i.getProductId().equals(productId));

        CartItemDto newItem = null;
        if (!alreadyInCart) {
            ProductEntity product = productRepository.findById(productId)
                    .orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND));

            String mainImagePath = productImageRepository.findMainImagePath(productId)
                    .orElse(null);

            String fullUrl = (mainImagePath == null) ? null :
                    fileStorageProperties.endpoint() + "/" +
                            fileStorageProperties.bucket() + "/" + mainImagePath;

            newItem = CartItemDto.builder()
                    .productId(productId)
                    .quantity(quantity)
                    .productName(product.getName())
                    .mainImageUrl(fullUrl)
                    .unitPrice(product.getPrice())
                    .build();
        }

        final CartItemDto itemToAdd = newItem;

        List<CartItemDto> cartItems = cartCache.asMap().compute(userId, (key, items) -> {
            List<CartItemDto> list = (items == null) ? new ArrayList<>() : items;

            Optional<CartItemDto> existing = list.stream()
                    .filter(i -> i.getProductId().equals(productId))
                    .findFirst();

            if (existing.isPresent()) {
                existing.get().setQuantity(existing.get().getQuantity() + quantity);
            } else if (itemToAdd != null) {
                list.add(itemToAdd);
            }
            return list;
        });

        log.info("ActionLog.addToCart.end");
        return CartResponseDto.builder()
                .cartItems(cartItems)
                .message("Product added to cart successfully")
                .userId(userId)
                .totalPrice(calculateTotalPrice(cartItems))
                .totalQuantity(calculateTotalQuantity(cartItems))
                .build();
    }

    @Override
    public CartResponseDto removeFromCart(Long userId, Long productId) {
        log.info("ActionLog.removeFromCart.start");
        AtomicBoolean removed = new AtomicBoolean(false);

        List<CartItemDto> cartItems = cartCache.asMap().computeIfPresent(userId, (key,items) -> {
            removed.set(items.removeIf(item -> item.getProductId().equals(productId)));
            return items;
        });
        if (cartItems == null) {
            throw new ApplicationException(ErrorCode.CART_EMPTY);
        }
        if (!removed.get()) {
            throw new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND_IN_CART);
        }
        log.info("ActionLog.removeFromCart.end");
        return CartResponseDto.builder()
                .cartItems(cartItems)
                .message("Product removed from cart successfully")
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
            cartItems = new ArrayList<>();
        }

        List<CartItemDto> snapshot;
        synchronized (cartItems) {
            snapshot = new ArrayList<>(cartItems);
        }

        log.info("ActionLog.getCart.end");
        return CartResponseDto.builder()
                .cartItems(snapshot)
                .message("Cart retrieved successfully")
                .userId(userId)
                .totalPrice(calculateTotalPrice(snapshot))
                .totalQuantity(calculateTotalQuantity(snapshot))
                .build();
    }

    @Override
    public CartResponseDto updateCart(Long userId, Long productId, int quantity) {
        log.info("ActionLog.updateCart.start");

        AtomicBoolean found = new AtomicBoolean(false);

        List<CartItemDto> cartItems = cartCache.asMap().computeIfPresent(userId, (key, items) -> {

            items.stream()
                    .filter(i -> i.getProductId().equals(productId))
                    .findFirst()
                    .ifPresent(item -> {
                        found.set(true);
                        item.setQuantity(quantity);
                    });

            return items;
        });

        if (cartItems == null) {
            throw new ApplicationException(ErrorCode.CART_EMPTY);
        }
        if (!found.get()) {
            throw new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND_IN_CART);
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
