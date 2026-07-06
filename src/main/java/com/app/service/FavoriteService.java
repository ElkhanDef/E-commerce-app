package com.app.service;

import com.app.model.dto.response.FavoriteProductDto;
import com.app.model.dto.response.FavoriteResponseDto;

import java.util.List;

public interface FavoriteService {

    FavoriteResponseDto toggleFavorite (Long productId, Long userId);

    List<FavoriteProductDto> getFavorites(Long userId);

}
