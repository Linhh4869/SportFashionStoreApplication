package com.example.sportfashionstore.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sportfashionstore.callback.DataStateCallback;
import com.example.sportfashionstore.imageupload.ImageProcessor;
import com.example.sportfashionstore.model.ImageUploadResult;
import com.example.sportfashionstore.repository.ImageRepository;

import java.io.File;

public class CameraViewModel extends ViewModel {
    private ImageRepository repository;
    private MutableLiveData<ImageUploadResult> uploadResult;
    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<File> capturedImage;

    public CameraViewModel() {
        repository = new ImageRepository();
        uploadResult = new MutableLiveData<>();
        isLoading = new MutableLiveData<>(false);
        capturedImage = new MutableLiveData<>();
    }

    public LiveData<ImageUploadResult> getUploadResult() {
        return uploadResult;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<File> getCapturedImage() {
        return capturedImage;
    }

    public void setCapturedImage(File imageFile) {
        capturedImage.setValue(imageFile);
    }

    public void processAndUploadImage(File originalImage, String outputDir) {
        isLoading.setValue(true);

        // Process image in background thread
        new Thread(() -> {
            try {
                // Process image (scale, rename)
                File processedImage = ImageProcessor.processAndSaveImage(
                        originalImage.getAbsolutePath(), outputDir);

                // Upload to Firebase
                repository.uploadImage(processedImage, new DataStateCallback<String>() {
                    @Override
                    public void onSuccess(String downloadUrl) {
                        isLoading.postValue(false);
                        uploadResult.postValue(new ImageUploadResult(true, downloadUrl, null));

                        // Clean up temporary files
                        cleanupFiles(originalImage, processedImage);
                    }

                    @Override
                    public void onError(String message) {
                        isLoading.postValue(false);
                        uploadResult.postValue(new ImageUploadResult(false, null, message));

                        // Clean up temporary files
                        cleanupFiles(originalImage, processedImage);
                    }
                });

            } catch (Exception e) {
                isLoading.postValue(false);
                uploadResult.postValue(new ImageUploadResult(false, null,
                        "Xử lý ảnh thất bại: " + e.getMessage()));

                // Clean up
                if (originalImage.exists()) originalImage.delete();
            }
        }).start();
    }

    private void cleanupFiles(File originalImage, File processedImage) {
        if (originalImage != null && originalImage.exists()) {
            originalImage.delete();
        }
        if (processedImage != null && processedImage.exists()) {
            processedImage.delete();
        }
    }

    public void clearCapturedImage() {
        File currentImage = capturedImage.getValue();
        if (currentImage != null && currentImage.exists()) {
            currentImage.delete();
        }
        capturedImage.setValue(null);
    }
}
