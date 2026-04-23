package com.app.service.impl;

import com.app.config.FileStorageProperties;
import com.app.exception.ApplicationException;
import com.app.exception.data.ErrorCode;
import com.app.mapper.CategoryMapper;
import com.app.mapper.ProductMapper;
import com.app.model.dto.request.PageableRequest;
import com.app.model.dto.request.ProductRequestDto;
import com.app.model.dto.response.FailedImageDto;
import com.app.model.dto.response.PartialImageUploadResponseDto;
import com.app.model.dto.response.ProductListResponseDto;
import com.app.model.dto.response.ProductResponseDto;
import com.app.model.dto.response.UploadedImageDto;
import com.app.model.entity.CategoryEntity;
import com.app.model.entity.ProductEntity;
import com.app.model.entity.ProductImageEntity;
import com.app.model.enums.SlugTarget;
import com.app.repository.CategoryRepository;
import com.app.repository.ProductImageRepository;
import com.app.repository.ProductRepository;
import com.app.service.ProductService;
import com.app.utils.CommonUtils;
import com.app.validator.ImageFileValidator;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final MinioClient minioClient;
    private final SlugGeneratorService slugGenerator;
    private final ImageFileValidator imageValidator;
    private final ProductImageRepository productImageRepository;
    private final FileStorageProperties fileStorageProperties;

    public ProductServiceImpl(ProductRepository productRepository,
                              MinioClient minioClient,
                              CategoryRepository categoryRepository,
                              SlugGeneratorService slugGenerator,
                              ImageFileValidator imageValidator,
                              ProductImageRepository productImageRepository,
                              FileStorageProperties fileStorageProperties) {
        this.productRepository = productRepository;
        this.minioClient = minioClient;
        this.categoryRepository = categoryRepository;
        this.slugGenerator = slugGenerator;
        this.imageValidator = imageValidator;
        this.productImageRepository = productImageRepository;
        this.fileStorageProperties = fileStorageProperties;
    }

    @Override
    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
        log.info("ActionLog.createProduct.start");
        CategoryEntity category = categoryRepository.findById(productRequestDto.getCategoryId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.CATEGORY_NOT_FOUND));

        ProductEntity product = ProductMapper.INSTANCE.toEntity(productRequestDto);
        product.setSlug(slugGenerator.generateUniqueSlug(productRequestDto.getName(), SlugTarget.PRODUCT));
        product.setCategory(category);
        ProductEntity savedProduct = productRepository.save(product);

        ProductResponseDto response = ProductMapper.INSTANCE.toDto(savedProduct);
        response.setCategoryResponse(CategoryMapper.INSTANCE.toDto(category));
        log.info("ActionLog.createProduct.end");
        return response;
    }

    @Override
    @Transactional
    public PartialImageUploadResponseDto uploadImages(Long productId,
                                                      List<MultipartFile> images) {
        log.info("ActionLog.uploadImages.start");
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND));

        List<ProductImageEntity> productImages = new ArrayList<>();
        List<UploadedImageDto> uploadedList = new ArrayList<>();
        List<FailedImageDto> failedList = new ArrayList<>();

        String bucket = fileStorageProperties.bucket();

        for (MultipartFile image : images) {
            String fileName = image.getOriginalFilename();
            String uniqueFileName = CommonUtils.generateUniqueImageName(fileName);

            try {
                imageValidator.validateImage(image);
                String path = bucket + "/" + productId + "/" + uniqueFileName;

                minioClient.putObject(
                        PutObjectArgs.builder()
                                .stream(image.getInputStream(), image.getSize(), -1)
                                .object(path)
                                .contentType(image.getContentType())
                                .bucket(bucket)
                                .build()
                );
                ProductImageEntity entity = new ProductImageEntity(
                        path,
                        false,
                        uniqueFileName,
                        product
                );
                productImages.add(entity);

            } catch (ApplicationException ex) {
                failedList.add(
                        FailedImageDto.builder()
                                .filename(uniqueFileName)
                                .error(ex.getMessage())
                                .errorCode(ex.getErrorCode().name())
                                .build()
                );
                //CHECKSTYLE:OFF
            } catch (Exception ex) {
                log.error("ActionLog.uploadImages.Image upload failed", ex);
                failedList.add(
                        FailedImageDto.builder()
                                .filename(uniqueFileName)
                                .error("Upload failed")
                                .errorCode("IMAGE_UPLOAD_FAILED")
                                .build()
                );
            } //CHECKSTYLE:ON
        }
        productImageRepository.saveAll(productImages);
        //product.getImages().addAll(productImages);

        for (ProductImageEntity entity : productImages) {
            uploadedList.add(
                    UploadedImageDto.builder()
                            .id(entity.getId())
                            .url(fileStorageProperties.endpoint() + "/" + entity.getImagePath())
                            .fileName(entity.getFileName())
                            .main(entity.isMain())
                            .build()
            );
        }
        log.info("ActionLog.uploadImages.end");
        return PartialImageUploadResponseDto.builder()
                .productId(productId)
                .uploaded(uploadedList)
                .failed(failedList)
                .totalUploaded(uploadedList.size())
                .totalFailed(failedList.size())
                .build();
    }

    @Override
    public Page<ProductListResponseDto> getAllProducts(PageableRequest request) {
        log.info("ActionLog.getAllProducts.start");
        Pageable pageable = request.toPageable();
        log.info("ActionLog.getAllProducts.end");
        return productRepository.findAll(pageable).map(ProductMapper.INSTANCE::toDtoList);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDto getProductById(Long productId) {
        log.info("ActionLog.getProductById.start");
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND));
        log.info("ActionLog.getProductById.end");
        return buildProductResponse(product);
    }

    @Override
    @Transactional
    public ProductResponseDto selectMainImage(Long productId, Long imageId) {
        log.info("ActionLog.selectMainImage.start");
        productImageRepository.unsetAllMainImages(productId);
        int changedRow = productImageRepository.setMainImage(productId, imageId);

        if (changedRow == 0) {
            throw new ApplicationException(ErrorCode.IMAGE_NOT_BELONG_TO_PRODUCT);
        }
        ProductEntity product = productRepository.findById(productId).get();
        log.info("ActionLog.selectMainImage.end");
        return buildProductResponse(product);
    }

    @Override
    public ProductResponseDto getProductBySlug(String slug) {
        log.info("ActionLog.getProductBySlug.start");
        ProductEntity product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND));
        log.info("ActionLog.getProductBySlug.end");
        return buildProductResponse(product);
    }

    @Override
    public void deleteProductById(Long id) {
        log.info("ActionLog.deleteProductById.start");
        ProductEntity product = productRepository.findById(id)
                        .orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND));
        productRepository.delete(product);
        log.info("ActionLog.deleteProductById.end");
    }

    private ProductResponseDto buildProductResponse(ProductEntity product) {
        log.info("ProductResponseDto.buildProductResponse.start");
        String baseUrl = fileStorageProperties.endpoint() + "/";

        List<String> imageUrls = new ArrayList<>();
        String mainImageUrl = null;

        for (ProductImageEntity image : product.getImages()) {
            String fullUrl = baseUrl + image.getImagePath();
            imageUrls.add(fullUrl);

            if(image.isMain()) {
                mainImageUrl = fullUrl;
            }
        }

        ProductResponseDto response = ProductMapper.INSTANCE.toDto(product);
        response.setCategoryResponse(CategoryMapper.INSTANCE.toDto(product.getCategory()));
        response.setImageUrls(imageUrls);
        response.setMainImageUrl(mainImageUrl);
        log.info("ProductResponseDto.buildProductResponse.end");
        return response;
    }
}
