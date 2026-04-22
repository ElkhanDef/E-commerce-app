package com.app.service;

import com.app.model.dto.request.PageableRequest;
import com.app.model.dto.request.ProductRequestDto;
import com.app.model.dto.response.PartialImageUploadResponseDto;
import com.app.model.dto.response.ProductListResponseDto;
import com.app.model.dto.response.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    ProductResponseDto createProduct(ProductRequestDto productRequestDto);
    PartialImageUploadResponseDto uploadImages(Long productId, List<MultipartFile> images);
    Page<ProductListResponseDto> getAllProducts(PageableRequest request);
    ProductResponseDto getProductById(Long productId);
    ProductResponseDto selectMainImage(Long productId, Long imageId);
}
