package com.example.sportfashionstore.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.example.sportfashionstore.app.MyApplication;
import com.example.sportfashionstore.commonbase.BaseViewModel;
import com.example.sportfashionstore.model.FeatureModel;
import com.example.sportfashionstore.repository.UtilityRepository;
import com.example.sportfashionstore.util.SharePrefHelper;

import java.util.ArrayList;
import java.util.List;

public class PersonalViewModel extends BaseViewModel {
    private final SharePrefHelper sharePrefHelper;
    private MutableLiveData<String> userName = new MutableLiveData<>("");
    private UtilityRepository utilityRepository = new UtilityRepository();
    private MutableLiveData<List<FeatureModel>> accFeatureList = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<List<FeatureModel>> commonFeatureList = new MutableLiveData<>(new ArrayList<>());

    public PersonalViewModel() {
        sharePrefHelper = MyApplication.getSharePrefHelper();
        userName.setValue(sharePrefHelper.getUserName());
        accFeatureList.setValue(utilityRepository.getAccountSettingList());
        commonFeatureList.setValue(utilityRepository.getCommonSettingList());
    }

    public MutableLiveData<String> getUserName() {
        return userName;
    }

    public void onUserNameChange(String newName) {
        userName.setValue(newName);
    }

    public MutableLiveData<List<FeatureModel>> getAccFeatureList() {
        return accFeatureList;
    }

    public MutableLiveData<List<FeatureModel>> getCommonFeatureList() {
        return commonFeatureList;
    }

    public void onAccFeatureListChange(List<FeatureModel> newList) {
        accFeatureList.setValue(newList);
    }

    public void onCommonFeatureListChange(List<FeatureModel> newList) {
        accFeatureList.setValue(newList);
    }


}
