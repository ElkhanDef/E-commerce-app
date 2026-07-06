package com.app.service.impl;

import com.app.config.FileStorageProperties;
import com.app.exception.ApplicationException;
import com.app.exception.data.ErrorCode;
import com.app.model.dto.response.FavoriteProductDto;
import com.app.model.dto.response.FavoriteResponseDto;
import com.app.model.entity.FavoriteEntity;
import com.app.model.entity.ProductEntity;
import com.app.model.entity.ProductImageEntity;
import com.app.model.entity.UserEntity;
import com.app.repository.FavoriteRepository;
import com.app.repository.ProductImageRepository;
import com.app.repository.ProductRepository;
import com.app.repository.UserRepository;
import com.app.service.FavoriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FavoriteServiceImpl implements FavoriteService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;
    private final ProductImageRepository productImageRepository;
    private final FileStorageProperties fileStorageProperties;

    public FavoriteServiceImpl(ProductRepository productRepository, UserRepository userRepository,
                               FavoriteRepository favoriteRepository, ProductImageRepository productImageRepository,
                               FileStorageProperties fileStorageProperties) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.favoriteRepository = favoriteRepository;
        this.productImageRepository = productImageRepository;
        this.fileStorageProperties = fileStorageProperties;
    }

    @Override
    @Transactional
    public FavoriteResponseDto toggleFavorite(Long productId, Long userId) {
        log.info("ActionLog.toggleFavorite.start");
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND));

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        Optional<FavoriteEntity> optionalFavorite =
                favoriteRepository.findByUserIdAndProductId(userId, productId);

        String message;
        Long favoriteId;

        if (optionalFavorite.isPresent()) {
            FavoriteEntity favorite = optionalFavorite.get();
            favoriteId = favorite.getId();
            favoriteRepository.delete(favorite);
            message = "Product removed from favorites";
        } else {
            FavoriteEntity favorite = new FavoriteEntity();
            favorite.setUser(user);
            favorite.setProduct(product);

            favorite = favoriteRepository.save(favorite);
            favoriteId = favorite.getId();

            message = "Product added to favorites";
        }

        String baseUrl = fileStorageProperties.endpoint() + "/" +
                fileStorageProperties.bucket() + "/";

        String mainImageThumbPath = productImageRepository
                .findMainImageThumbnailPath(productId)
                .orElse(null);

        String mainImageThumbUrl =
                mainImageThumbPath != null ? baseUrl + mainImageThumbPath : null;

        FavoriteProductDto dto = FavoriteProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .mainImageUrl(mainImageThumbUrl)
                .build();
        log.info("ActionLog.toggleFavorite.end");
        return FavoriteResponseDto.builder()
                .id(favoriteId)
                .product(dto)
                .message(message)
                .build();
    }

    @Transactional(readOnly = true)
    public List<FavoriteProductDto> getFavorites(Long userId) {
        log.info("ActionLog.getFavorites.start");
        List<FavoriteEntity> favorites = favoriteRepository.findByUserId(userId);

        String baseUrl = fileStorageProperties.endpoint() + "/" +
                fileStorageProperties.bucket() + "/";

        List<FavoriteProductDto> result = favorites.stream()
                .map(fav -> {
                    ProductEntity product = fav.getProduct();

                    String thumbPath = product.getImages().stream()
                            .filter(ProductImageEntity::isMain)
                            .findFirst()
                            .map(ProductImageEntity::getThumbPath)
                            .orElse(null);

                    String imageUrl = thumbPath != null ? baseUrl + thumbPath : null;

                    return FavoriteProductDto.builder()
                            .id(product.getId())
                            .name(product.getName())
                            .price(product.getPrice())
                            .mainImageUrl(imageUrl)
                            .build();
                })
                .toList();

        log.info("ActionLog.getFavorites.end");
        return result;
    }
}
