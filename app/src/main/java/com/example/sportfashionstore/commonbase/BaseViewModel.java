package com.example.sportfashionstore.commonbase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public abstract class BaseViewModel extends ViewModel {
    protected final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    protected final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<Boolean> getLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    protected <T> void setLoadingState(MutableLiveData<Resource<T>> liveData) {
        liveData.setValue(Resource.loading(null));
    }

    protected <T> void setSuccessState(MutableLiveData<Resource<T>> liveData, T data) {
        liveData.setValue(Resource.success(data));
    }

    protected <T> void setErrorState(MutableLiveData<Resource<T>> liveData, String message) {
        liveData.setValue(Resource.error(message, null));
    }
}
