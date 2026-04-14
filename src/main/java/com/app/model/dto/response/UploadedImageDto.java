package com.app.model.dto.response;

public class UploadedImageDto {

    private Long id;
    private String url;
    private String fileName;
    private boolean main;

    public UploadedImageDto(Long id, String url, String fileName, boolean main) {
        this.id = id;
        this.url = url;
        this.fileName = fileName;
        this.main = main;
    }
    public UploadedImageDto() {}

    public static Builder builder(){
        return new Builder();
    }

    public String getUrl() {return url;}
    public void setUrl(String url) {this.url = url;}
    public String getFileName() {return fileName;}
    public void setFileName(String fileName) {this.fileName = fileName;}
    public boolean isMain() {return main;}
    public void setMain(boolean main) {this.main = main;}
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    @SuppressWarnings("checkstyle:HiddenField")
    public static class Builder {
        private Long id;
        private String url;
        private String fileName;
        private boolean main;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder url(String url) {
            this.url = url;
            return this;
        }
        public Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }
        public Builder main(boolean main) {
            this.main = main;
            return this;
        }
        public UploadedImageDto build() {
            return new UploadedImageDto(id,url,fileName,main);
        }
    }
}
