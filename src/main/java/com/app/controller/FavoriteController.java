package com.app.controller;

import com.app.model.dto.response.FavoriteResponseDto;
import com.app.service.FavoriteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping(path = "/{productId}/toggle")
    public ResponseEntity<FavoriteResponseDto> toggleFavorite (@PathVariable Long productId,
                                                               @AuthenticationPrincipal String userId){
        return ResponseEntity.ok(favoriteService.toggleFavorite(productId, Long.valueOf(userId)));
    }
}
