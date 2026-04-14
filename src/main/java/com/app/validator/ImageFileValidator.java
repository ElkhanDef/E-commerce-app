package com.app.validator;

import com.app.config.FileProperties;
import com.app.exception.ApplicationException;
import com.app.exception.data.ErrorCode;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Component
public class ImageFileValidator {

    private final FileProperties fileProperties;

    public ImageFileValidator(FileProperties fileProperties) {
        this.fileProperties = fileProperties;
    }

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg",
            "image/png"
    );

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            ".jpg", ".jpeg", ".png"
    );

    public void validateImageFiles(List<MultipartFile> imageFiles) {

        if (imageFiles == null || imageFiles.isEmpty()) {
            throw new ApplicationException(ErrorCode.NO_IMAGES_PROVIDED);
        }
        if (imageFiles.size() > 4) {
            throw new ApplicationException(ErrorCode.TOO_MANY_IMAGES);
        }
        for (MultipartFile imageFile : imageFiles) {
            validateImage(imageFile);
        }
    }

    public void validateImage(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new ApplicationException(ErrorCode.FILE_EMPTY);
        }

        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.isEmpty()) {
            throw new ApplicationException(ErrorCode.FILE_EMPTY);
        }
        if (originalFileName.length() > 255) {
            throw new ApplicationException(ErrorCode.INVALID_FILENAME);
        }
        if (file.getSize() > fileProperties.maxFileSize()) {
            throw new ApplicationException(ErrorCode.FILE_TOO_LARGE);
        }
        String contentType = file.getContentType();
        if (!ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new ApplicationException(ErrorCode.INVALID_FILE_TYPE);
        }
        String extension = getFileExtension(originalFileName).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new ApplicationException(ErrorCode.INVALID_FILE_EXTENSION);
        }
    }

    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        return lastDotIndex > 0 ? filename.substring(lastDotIndex) : "";
    }
}
