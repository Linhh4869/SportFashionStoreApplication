package com.example.sportfashionstore.commonbase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public abstract class BaseViewModel extends ViewModel {
    protected final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    protected final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<Boolean> getLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    protected <T> void setLoadingState(MutableLiveData<Resource<T>> liveData) {
        isLoading.setValue(true);
        errorMessage.setValue("");
        liveData.setValue(Resource.loading(null));
    }

    protected <T> void setSuccessState(MutableLiveData<Resource<T>> liveData, T data) {
        isLoading.setValue(false);
        errorMessage.setValue("");
        liveData.setValue(Resource.success(data));
    }

    protected <T> void setErrorState(MutableLiveData<Resource<T>> liveData, String message) {
        isLoading.setValue(false);
        errorMessage.setValue(message);
        liveData.setValue(Resource.error(message, null));
    }
}
