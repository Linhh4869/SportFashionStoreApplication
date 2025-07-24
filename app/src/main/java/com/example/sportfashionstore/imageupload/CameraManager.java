package com.example.sportfashionstore.imageupload;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;

public class CameraManager {
    public static final int CAMERA_PERMISSION_REQUEST = 100;
    public static final int CAMERA_REQUEST_CODE = 101;

    private final Activity activity;
    private File currentPhotoFile;

    public interface CameraCallback {
        void onPermissionDenied();
        void onCameraError(String error);
        void onImageCaptured(File imageFile);
    }

    public CameraManager(Activity activity) {
        this.activity = activity;
    }

    public void openCamera(CameraCallback callback) {
        if (checkCameraPermission()) {
            startCamera(callback);
        } else {
            requestCameraPermission();
        }
    }

    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.CAMERA},
                CAMERA_PERMISSION_REQUEST);
    }

    private void startCamera(CameraCallback callback) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            try {
                currentPhotoFile = createImageFile();

                Uri photoURI = FileProvider.getUriForFile(activity,
                        activity.getPackageName() + ".fileprovider",
                        currentPhotoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                activity.startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);

            } catch (IOException ex) {
                callback.onCameraError("Không thể tạo file ảnh: " + ex.getMessage());
            }
        } else {
            callback.onCameraError("Không tìm thấy ứng dụng camera");
        }
    }

    private File createImageFile() throws IOException {
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile("CAMERA_", ".jpg", storageDir);
    }

    public void handleActivityResult(int requestCode, int resultCode, Intent data,
                                     CameraCallback callback) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && currentPhotoFile != null) {
                callback.onImageCaptured(currentPhotoFile);
            } else {
                callback.onCameraError("Chụp ảnh bị hủy hoặc thất bại");
            }
        }
    }

    public void handlePermissionResult(int requestCode, String[] permissions,
                                       int[] grantResults, CameraCallback callback) {
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera(callback);
            } else {
                callback.onPermissionDenied();
            }
        }
    }
}
