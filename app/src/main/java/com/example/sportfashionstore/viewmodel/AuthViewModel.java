package com.example.sportfashionstore.viewmodel;

import android.content.Intent;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.sportfashionstore.app.MyApplication;
import com.example.sportfashionstore.commonbase.BaseViewModel;
import com.example.sportfashionstore.callback.DataStateCallback;
import com.example.sportfashionstore.commonbase.Resource;
import com.example.sportfashionstore.model.User;
import com.example.sportfashionstore.repository.AuthRepository;
import com.example.sportfashionstore.util.Constants;
import com.example.sportfashionstore.util.SharePrefHelper;
import com.example.sportfashionstore.util.StringUtil;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;

public class AuthViewModel extends BaseViewModel {
    private final AuthRepository authRepository = new AuthRepository();
    private final SharePrefHelper sharePrefHelper;
    private final MutableLiveData<Resource<FirebaseUser>> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<Resource<User>> userLoginLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> emailLogin = new MutableLiveData<>("");
    private final MutableLiveData<String> email = new MutableLiveData<>("");
    private final MutableLiveData<String> password = new MutableLiveData<>("");
    private final MutableLiveData<String> displayName = new MutableLiveData<>("");
    private final MutableLiveData<String> address = new MutableLiveData<>("");
    private final MutableLiveData<Boolean> hideErrorEmail = new MutableLiveData<>(true);
    private final MutableLiveData<Boolean> hideErrorPassword = new MutableLiveData<>(true);
    private final MutableLiveData<Boolean> hideErrorDisplayName = new MutableLiveData<>(true);
    private final MutableLiveData<Boolean> hideErrorAddress = new MutableLiveData<>(true);
    private final MutableLiveData<String> selectedRole = new MutableLiveData<>(Constants.Role.BUYER);

    public AuthViewModel() {
        sharePrefHelper = MyApplication.getSharePrefHelper();
        emailLogin.setValue(sharePrefHelper.getEmail());
    }

    public LiveData<Resource<FirebaseUser>> getUserLiveData() {
        return userLiveData;
    }

    public LiveData<Resource<User>> getUserLoginLiveData() {
        return userLoginLiveData;
    }

    public void loginWithEmail(String email, String password) {
        setLoadingState(userLiveData);
        authRepository.loginWithEmail(email, password, new DataStateCallback<>() {
            @Override
            public void onSuccess(User data) {
                setSuccessState(userLoginLiveData, data);
                if (data != null) {
                    sharePrefHelper.setLoggedIn(true);
                    sharePrefHelper.setUserName(data.getDisplayName());
                    sharePrefHelper.setEmail(data.getEmail());
                    sharePrefHelper.setRole(data.getRole());
                }
            }

            @Override
            public void onError(String message) {
                setErrorState(userLiveData, message);
            }
        });
    }

    public void loginWithCredential(AuthCredential credential) {
        setLoadingState(userLiveData);
        authRepository.loginWithCredential(credential, userLiveData);
    }
    
    public void onLogin() {
        String _email = emailLogin.getValue();
        String _password = password.getValue();
        hideErrorEmail.setValue(isEmailValid(_email));
        hideErrorPassword.setValue(isPasswordValid(_password));

        if (Boolean.TRUE.equals(hideErrorEmail.getValue()) && Boolean.TRUE.equals(hideErrorPassword.getValue())) {
            loginWithEmail(_email, _password);
        }
    }

    public void onRegister() {
        String _email = email.getValue();
        String _password = password.getValue();
        String _name = displayName.getValue();
        String _role = selectedRole.getValue();

        hideErrorEmail.setValue(isEmailValid(_email));
        hideErrorPassword.setValue(isPasswordValid(_password));
        hideErrorDisplayName.setValue(isDisplayNameValid(_name));

        if (Boolean.TRUE.equals(hideErrorEmail.getValue())
                && Boolean.TRUE.equals(hideErrorPassword.getValue())
                && Boolean.TRUE.equals(hideErrorDisplayName.getValue())
                && Boolean.TRUE.equals(hideErrorAddress.getValue())) {
            User registerUser = new User(_name, _email, _role);
            setLoadingState(userLiveData);
            authRepository.registerWithEmail(registerUser, _password, new DataStateCallback<>() {
                @Override
                public void onSuccess(FirebaseUser data) {
                    setSuccessState(userLiveData, data);
                    sharePrefHelper.setUserName(_name);
                    sharePrefHelper.setRole(_role);
                    sharePrefHelper.setLoggedIn(true);
                    sharePrefHelper.setEmail(_email);
                }

                @Override
                public void onError(String message) {
                    setErrorState(userLiveData, message);
                }
            });
        }
    }

    public Intent getGoogleSignInIntent() {
        return authRepository.getGoogleSignInIntent();
    }

    public void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        authRepository.handleGoogleSignInResult(completedTask)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        userLiveData.setValue(task.getResult());
//                    } else {
//                        errorLiveData.setValue(task.getException().getMessage());
//                    }
//                });
    }

    private boolean isEmailValid(String email) {
        return StringUtil.isNotNullAndEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        return StringUtil.isNotNullAndEmpty(password) && password.length() >= 6;
    }
    
    private boolean isDisplayNameValid(String name) {
        return StringUtil.isNotNullAndEmpty(name) && name.length() > 3;
    }
    
    private boolean isAddressValid(String address) {
        return StringUtil.isNotNullAndEmpty(address);
    }

    public MutableLiveData<String> getEmail() {
        return email;
    }

    public MutableLiveData<String> getEmailLogin() {
        return emailLogin;
    }

    public MutableLiveData<String> getPassword() {
        return password;
    }

    public MutableLiveData<String> getDisplayName() {
        return displayName;
    }

    public MutableLiveData<String> getAddress() {
        return address;
    }

    public LiveData<Boolean> getHideErrorEmail() {
        return hideErrorEmail;
    }

    public LiveData<Boolean> getHideErrorPassword() {
        return hideErrorPassword;
    }

    public LiveData<Boolean> getHideErrorAddress() {
        return hideErrorAddress;
    }

    public LiveData<Boolean> getHideErrorDisplayName() {
        return hideErrorDisplayName;
    }

    public void onEmailChanged(String newEmail) {
        email.setValue(newEmail);
    }

    public void onEmailLoginChanged(String newEmail) {
        email.setValue(newEmail);
    }

    public void onPasswordChanged(String newPassword) {
        password.setValue(newPassword);
    }

    public void onDisplayNameChanged(String newName) {
        displayName.setValue(newName);
    }

    public void onAddressChanged(String newAddress) {
        address.setValue(newAddress);
    }

    public MutableLiveData<String> getSelectedRole() {
        return selectedRole;
    }

    public void setSelectedRole(String role) {
        selectedRole.setValue(role);
    }
}
