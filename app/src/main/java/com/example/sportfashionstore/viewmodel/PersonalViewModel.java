package com.example.sportfashionstore.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.example.sportfashionstore.app.MyApplication;
import com.example.sportfashionstore.commonbase.BaseViewModel;
import com.example.sportfashionstore.util.SharePrefHelper;

public class PersonalViewModel extends BaseViewModel {
    private final SharePrefHelper sharePrefHelper;
    private MutableLiveData<String> userName = new MutableLiveData<>("");

    public PersonalViewModel() {
        sharePrefHelper = MyApplication.getSharePrefHelper();
        userName.setValue(sharePrefHelper.getUserName());
    }

    public MutableLiveData<String> getUserName() {
        return userName;
    }

    public void onUserNameChange(String newName) {
        userName.setValue(newName);
    }
}
