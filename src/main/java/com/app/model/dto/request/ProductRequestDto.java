package com.app.model.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class ProductRequestDto {

    @NotBlank(message = "Ürün adı boş olamaz")
    @Size(min = 3, max = 100, message = "Ürün adı 3 ile 100 karakter arasında olmalıdır")
    private String name;

    @NotNull(message = "Fiyat boş olamaz")
    @DecimalMin(value = "0.01", message = "Fiyat 0'dan büyük olmalıdır")
    @Digits(integer = 10, fraction = 2, message = "Fiyat formatı geçersiz (örnek: 2999.90)")
    private BigDecimal price;

    @NotNull(message = "Stok miktarı boş olamaz")
    @Min(value = 0, message = "Stok miktarı negatif olamaz")
    @Max(value = 999999, message = "Stok miktarı çok yüksek")
    private Integer stock;

    @NotBlank(message = "Stok Kodu (SKU) boş olamaz")
    @Size(min = 3, max = 50, message = "Stok Kodu (SKU) 3 ile 50 karakter arasında olmalıdır")
    private String sku;

    @Size(max = 2000, message = "Açıklama 2000 karakteri geçemez")
    private String description;

    @NotNull(message = "Kategori seçilmelidir")
    private Long categoryId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
