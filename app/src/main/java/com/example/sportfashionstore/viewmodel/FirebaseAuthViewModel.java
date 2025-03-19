package com.example.sportfashionstore.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.sportfashionstore.commonbase.BaseViewModel;
import com.example.sportfashionstore.commonbase.Resource;
import com.example.sportfashionstore.repository.FireBaseAuthRepository;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthViewModel extends BaseViewModel {
    private final FireBaseAuthRepository authRepository = new FireBaseAuthRepository();
    private final MutableLiveData<Resource<FirebaseUser>> userLiveData = new MutableLiveData<>();

    public LiveData<Resource<FirebaseUser>> getUserLiveData() {
        return userLiveData;
    }

    public void register(String email, String password, String displayName) {
        setLoadingState(userLiveData);
        authRepository.registerWithEmail(email, password, displayName, userLiveData);
    }

    public void loginWithEmail(String email, String password) {
        setLoadingState(userLiveData);
        authRepository.loginWithEmail(email, password, userLiveData);
    }

    public void loginWithCredential(AuthCredential credential) {
        setLoadingState(userLiveData);
        authRepository.loginWithCredential(credential, userLiveData);
    }
}
