package com.example.sportfashionstore.model;

import java.io.Serializable;

public class ImageUploadResult implements Serializable {
    private boolean success;
    private String imageUrl;
    private String errorMessage;

    public ImageUploadResult(boolean success, String imageUrl, String errorMessage) {
        this.success = success;
        this.imageUrl = imageUrl;
        this.errorMessage = errorMessage;
    }

    // Getters
    public boolean isSuccess() { return success; }
    public String getImageUrl() { return imageUrl; }
    public String getErrorMessage() { return errorMessage; }
}
