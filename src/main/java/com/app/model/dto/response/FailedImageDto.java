package com.app.model.dto.response;

public class FailedImageDto {

    private String filename;
    private String error;
    private String errorCode;

    public FailedImageDto(String filename, String error, String errorCode) {
        this.filename = filename;
        this.error = error;
        this.errorCode = errorCode;
    }

    public FailedImageDto() {}

    public static Builder  builder() {
        return new Builder();
    }

    public String getFilename() {return filename;}
    public void setFilename(String filename) {this.filename = filename;}
    public String getError() {return error;}
    public void setError(String error) {this.error = error;}
    public String getErrorCode() {return errorCode;}
    public void setErrorCode(String errorCode) {this.errorCode = errorCode;}

    @SuppressWarnings("checkstyle:HiddenField")
    public static class Builder {
        private String filename;
        private String error;
        private String errorCode;

        public Builder filename(String filename) {
            this.filename = filename;
            return this;
        }
        public Builder error(String error) {
            this.error = error;
            return this;
        }
        public Builder errorCode(String errorCode) {
            this.errorCode = errorCode;
            return this;
        }
        public FailedImageDto build() {
            return new FailedImageDto(filename, error, errorCode);
        }
    }
}
