package com.example.sportfashionstore.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.example.sportfashionstore.app.MyApplication;
import com.example.sportfashionstore.commonbase.BaseViewModel;
import com.example.sportfashionstore.callback.DataStateCallback;
import com.example.sportfashionstore.commonbase.Resource;
import com.example.sportfashionstore.model.FeatureModel;
import com.example.sportfashionstore.repository.AuthRepository;
import com.example.sportfashionstore.repository.UtilityRepository;
import com.example.sportfashionstore.util.SharePrefHelper;

import java.util.ArrayList;
import java.util.List;

public class PersonalViewModel extends BaseViewModel {
    private final SharePrefHelper sharePrefHelper;
    private final AuthRepository authRepository;
    private MutableLiveData<String> userName = new MutableLiveData<>("");
    private UtilityRepository utilityRepository = new UtilityRepository();
    private MutableLiveData<List<FeatureModel>> accFeatureList = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<List<FeatureModel>> commonFeatureList = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<Resource<String>> processLiveData = new MutableLiveData<>();

    public MutableLiveData<Resource<String>> getProcessLiveData() {
        return processLiveData;
    }

    public PersonalViewModel() {
        sharePrefHelper = MyApplication.getSharePrefHelper();
        authRepository = new AuthRepository();
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

    public void handleAccountSettingFeature(FeatureModel feature) {
        switch (feature.getTypeSetting()) {
            case CHANGE_ROLE:
            case EDIT_INFORMATION:
            case CHANGE_PASSWORD:
            case DELETE_ACCOUNT:
            case LANGUAGE:
            case THEME:
                break;
            case SIGN_OUT:
                onSignOut();
                break;
        }
    }

    private void onSignOut() {
        setLoadingState(processLiveData);
        authRepository.signOut(new DataStateCallback<>() {
            @Override
            public void onSuccess(String data) {
                setSuccessState(processLiveData, data);
                sharePrefHelper.setLoggedIn(false);
            }

            @Override
            public void onError(String message) {
                setErrorState(processLiveData, message);
            }
        });
    }

    private void changePassword(String currentPassword, String newPassword) {
        authRepository.changePassword(sharePrefHelper.getEmail(), currentPassword, newPassword, new DataStateCallback<>() {
            @Override
            public void onSuccess(String data) {
            }

            @Override
            public void onError(String message) {

            }
        });
    }
}
