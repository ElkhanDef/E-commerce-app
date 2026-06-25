package com.app.controller;

import com.app.model.dto.request.PageableRequest;
import com.app.model.dto.request.ProductRequestDto;
import com.app.model.dto.response.PartialImageUploadResponseDto;
import com.app.model.dto.response.ProductListResponseDto;
import com.app.model.dto.response.ProductResponseDto;
import com.app.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Page<ProductListResponseDto>> getAllProducts(@ModelAttribute @Valid PageableRequest req) {
        return ResponseEntity.ok(productService.getAllProducts(req));
    }

    @GetMapping(path = "/{slug}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable String slug) {
        return ResponseEntity.ok(productService.getProductBySlug(slug));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/management")
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody @Valid ProductRequestDto productRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(productRequestDto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/management/{productId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PartialImageUploadResponseDto> uploadImages(@PathVariable Long productId,
                                                                      @RequestParam("files") List<MultipartFile> files) {
        return ResponseEntity.ok(productService.uploadImages(productId, files));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/management/{productId}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/management/{productId}/images/{imageId}/main")
    public ResponseEntity<ProductResponseDto> selectMainImage(@PathVariable Long productId,
                                                              @PathVariable Long imageId) {
        return ResponseEntity.ok(productService.selectMainImage(productId, imageId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/management/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id) {
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }
}
