package com.app.service;

import com.app.model.dto.request.ProductRequestDto;
import com.app.model.dto.response.PartialImageUploadResponseDto;
import com.app.model.dto.response.ProductResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    ProductResponseDto createProduct(ProductRequestDto productRequestDto);
    PartialImageUploadResponseDto uploadImages(Long productId, List<MultipartFile> images);
}
