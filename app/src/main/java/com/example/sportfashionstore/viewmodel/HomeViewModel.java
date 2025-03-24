package com.example.sportfashionstore.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.sportfashionstore.app.MyApplication;
import com.example.sportfashionstore.commonbase.BaseViewModel;
import com.example.sportfashionstore.util.SharePrefHelper;

public class HomeViewModel extends BaseViewModel {
    private final SharePrefHelper sharePrefHelper;
    private MutableLiveData<String> userName = new MutableLiveData<>("");

    public HomeViewModel() {
        this.sharePrefHelper = MyApplication.getSharePrefHelper();
        userName.setValue(this.sharePrefHelper.getUserName());
    }

    public MutableLiveData<String> getUserName() {
        return userName;
    }

    public void onUserNameChange(String newName) {
        userName.setValue(newName);
    }
}
