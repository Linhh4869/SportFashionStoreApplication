package com.example.sportfashionstore.base;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BaseViewModel extends ViewModel {
    protected MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    protected MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<Boolean> isLoading() {
        return isLoading;
    }

    protected void setLoading(boolean loading) {
        isLoading.setValue(loading);
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(MutableLiveData<String> errorMessage) {
        this.errorMessage = errorMessage;
    }
}
