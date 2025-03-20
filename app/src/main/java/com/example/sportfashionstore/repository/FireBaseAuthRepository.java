package com.example.sportfashionstore.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.sportfashionstore.commonbase.Resource;
import com.example.sportfashionstore.model.User;
import com.example.sportfashionstore.util.Constants;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class FireBaseAuthRepository {
    private static final String TAG = "FirebaseAuthRepo";
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public void registerWithEmail(String email, String password, String displayName,
                                  MutableLiveData<Resource<FirebaseUser>> userLiveData) {
        userLiveData.setValue(Resource.loading(null));

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        saveUserToFirestore(user, displayName);
                        userLiveData.setValue(Resource.success(user));
                    } else {
                        userLiveData.setValue(Resource.error(task.getException().getMessage(), null));
                    }
                });
    }

    public void loginWithEmail(String email, String password,
                               MutableLiveData<Resource<FirebaseUser>> userLiveData) {
        userLiveData.setValue(Resource.loading(null));

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        userLiveData.setValue(Resource.success(firebaseAuth.getCurrentUser()));
                    } else {
                        userLiveData.setValue(Resource.error(task.getException().getMessage(), null));
                    }
                });
    }

    public void loginWithCredential(AuthCredential credential,
                                    MutableLiveData<Resource<FirebaseUser>> userLiveData) {
        userLiveData.setValue(Resource.loading(null));

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        saveUserToFirestore(user, user.getDisplayName());
                        userLiveData.setValue(Resource.success(user));
                    } else {
                        userLiveData.setValue(Resource.error(task.getException().getMessage(), null));
                    }
                });
    }

    private void saveUserToFirestore(FirebaseUser user, String displayName) {
        if (user == null) return;

        String uid = user.getUid();
        User newUser = new User(
                uid,
                user.getEmail(),
                displayName,
                user.getPhoneNumber(),
                "", // Address trống
                user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : "",
                Constants.Role.BUYER,
                System.currentTimeMillis(),
                System.currentTimeMillis()
        );

        firestore.collection(Constants.Collection.USERS)
                .document(uid)
                .set(newUser)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "User added to Firestore"))
                .addOnFailureListener(e -> Log.e(TAG, "Error adding user", e));
    }
}
