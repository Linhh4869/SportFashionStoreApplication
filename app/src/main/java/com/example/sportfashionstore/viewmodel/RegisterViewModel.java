package com.example.sportfashionstore.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.sportfashionstore.commonbase.BaseViewModel;
import com.example.sportfashionstore.commonbase.DataStateCallback;
import com.example.sportfashionstore.commonbase.Resource;
import com.example.sportfashionstore.repository.FireBaseAuthRepository;
import com.google.firebase.auth.FirebaseUser;

public class RegisterViewModel extends BaseViewModel {
    private final FireBaseAuthRepository authRepository = new FireBaseAuthRepository();
    private final MutableLiveData<Resource<FirebaseUser>> userLiveData = new MutableLiveData<>();

    public LiveData<Resource<FirebaseUser>> getUserLiveData() {
        return userLiveData;
    }

    public void register(String email, String password, String displayName, String address) {
        setLoadingState(userLiveData);
        authRepository.registerWithEmail(email, password, displayName, address, new DataStateCallback<>() {
            @Override
            public void onSuccess(FirebaseUser data) {
                setSuccessState(userLiveData, data);
            }

            @Override
            public void onError(String message) {
                setErrorState(userLiveData, message);
            }
        });
    }
}
