package com.app.controller;

import com.app.model.dto.request.PageableRequest;
import com.app.model.dto.response.ProductListResponseDto;
import com.app.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Page<ProductListResponseDto>> getAllProducts(@ModelAttribute @Valid PageableRequest req) {
        return ResponseEntity.ok(productService.getAllProducts(req));
    }
}
