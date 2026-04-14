package com.app.model.dto.response;

import java.util.List;

public class PartialImageUploadResponseDto {

    private Long productId;
    private List<UploadedImageDto> uploaded;
    private List<FailedImageDto> failed;
    private int totalUploaded;
    private int totalFailed;

    public PartialImageUploadResponseDto(Long productId, List<FailedImageDto> failed,
                                         List<UploadedImageDto> uploaded, int totalUploaded, int totalFailed) {
        this.productId = productId;
        this.failed = failed;
        this.uploaded = uploaded;
        this.totalUploaded = totalUploaded;
        this.totalFailed = totalFailed;
    }

    public PartialImageUploadResponseDto() {}

    public static Builder builder() {
        return new Builder();
    }

    public Long getProductId() {return productId;}
    public void setProductId(Long productId) {this.productId = productId;}
    public List<UploadedImageDto> getUploaded() {return uploaded;}
    public void setUploaded(List<UploadedImageDto> uploaded) {this.uploaded = uploaded;}
    public List<FailedImageDto> getFailed() {return failed;}
    public void setFailed(List<FailedImageDto> failed) {this.failed = failed;}
    public int getTotalUploaded() {return totalUploaded;}
    public void setTotalUploaded(int totalUploaded) {this.totalUploaded = totalUploaded;}
    public int getTotalFailed() {return totalFailed;}
    public void setTotalFailed(int totalFailed) {this.totalFailed = totalFailed;}

    @SuppressWarnings("checksytle:HiddenField")
    public static class Builder {
        private Long productId;
        private List<UploadedImageDto> uploaded;
        private List<FailedImageDto> failed;
        private int totalUploaded;
        private int totalFailed;

        public Builder productId(Long productId) {
            this.productId = productId;
            return this;
        }
        public Builder failed(List<FailedImageDto> failed) {
            this.failed = failed;
            return this;
        }
        public Builder uploaded(List<UploadedImageDto> uploaded) {
            this.uploaded = uploaded;
            return this;
        }
        public Builder totalUploaded(int totalUploaded) {
            this.totalUploaded = totalUploaded;
            return this;
        }
        public Builder totalFailed(int totalFailed) {
            this.totalFailed = totalFailed;
            return this;
        }
        public PartialImageUploadResponseDto build() {
            return new PartialImageUploadResponseDto(productId, failed, uploaded, totalUploaded, totalFailed);
        }
    }
}
