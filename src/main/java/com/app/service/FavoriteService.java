package com.app.service;

import com.app.model.dto.response.FavoriteResponseDto;

public interface FavoriteService {

    FavoriteResponseDto toggleFavorite (Long productId, Long userId);

}
