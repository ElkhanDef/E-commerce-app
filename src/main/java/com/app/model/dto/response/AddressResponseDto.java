package com.app.model.dto.response;

public class AddressResponseDto {

    private Long id;
    private String city;
    private String district;
    private String fullAddress;
    private String postalCode;

    public AddressResponseDto() {}

    public AddressResponseDto(Long id, String city, String district,
                              String fullAddress, String postalCode) {
        this.id = id;
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
        private Long id;
        private String city;
        private String district;
        private String fullAddress;
        private String postalCode;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
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
        public AddressResponseDto build() {
            return new AddressResponseDto(id, city, district, fullAddress, postalCode);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }
    public String getFullAddress() { return fullAddress; }
    public void setFullAddress(String fullAddress) { this.fullAddress = fullAddress; }
    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
}
