package com.example.sportfashionstore.repository;

import android.net.Uri;

import com.example.sportfashionstore.callback.DataStateCallback;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ImageRepository {
    private FirebaseStorage storage;
    private StorageReference storageRef;

    @Inject
    public ImageRepository() {
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }

    public void uploadImage(File imageFile, DataStateCallback<String> callback) {
        String fileName = imageFile.getName();
        StorageReference imageRef = storageRef.child("image/" + fileName);

        UploadTask uploadTask = imageRef.putFile(Uri.fromFile(imageFile));

        uploadTask.addOnSuccessListener(taskSnapshot -> {
            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                callback.onSuccess(uri.toString());
            }).addOnFailureListener(e -> {
                callback.onError("Không thể lấy URL download: " + e.getMessage());
            }).addOnFailureListener(e -> {
                callback.onError("Không thể lấy URL download: " + e.getMessage());
            });
        });
    }
}
