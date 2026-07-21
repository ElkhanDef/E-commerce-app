package com.app.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AddressRequestDto {

    @NotBlank(message = "Şehir boş olamaz")
    @Size(max = 100, message = "Şehir adı en fazla 100 karakter olabilir")
    private String city;

    @NotBlank(message = "İlçe boş olamaz")
    @Size(max = 100, message = "İlçe adı en fazla 100 karakter olabilir")
    private String district;

    @NotBlank(message = "Adres boş olamaz")
    @Size(max = 500, message = "Adres en fazla 500 karakter olabilir")
    private String fullAddress;

    @NotBlank(message = "Posta kodu boş olamaz")
    @Size(min = 5, max = 5, message = "Posta kodu 5 karakter olmalıdır")
    private String postalCode;

    public AddressRequestDto() {}

    public AddressRequestDto(String city, String district,
                             String fullAddress, String postalCode) {
        this.city = city;
        this.district = district;
        this.fullAddress = fullAddress;
        this.postalCode = postalCode;
    }

    public static Builder builder() {
        return new Builder();
    }

    @SuppressWarnings("checkstyle:HiddenField")
    public static class Builder {
        private String city;
        private String district;
        private String fullAddress;
        private String postalCode;

        public Builder city(String city) {
            this.city = city;
            return this;
        }
        public Builder district(String district) {
            this.district = district;
            return this;
        }
        public Builder fullAddress(String fullAddress) {
            this.fullAddress = fullAddress;
            return this;
        }
        public Builder postalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }
        public AddressRequestDto build() {
            return new AddressRequestDto(city, district, fullAddress, postalCode);
        }
    }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }
    public String getFullAddress() { return fullAddress; }
    public void setFullAddress(String fullAddress) { this.fullAddress = fullAddress; }
    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
}
