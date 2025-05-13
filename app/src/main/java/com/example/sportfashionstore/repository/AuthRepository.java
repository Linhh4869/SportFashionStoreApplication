package com.example.sportfashionstore.repository;

import android.content.Intent;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.sportfashionstore.app.AppContextProvider;
import com.example.sportfashionstore.callback.DataStateCallback;
import com.example.sportfashionstore.commonbase.Resource;
import com.example.sportfashionstore.model.User;
import com.example.sportfashionstore.util.Constants;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class AuthRepository {
    private static final String TAG = "FirebaseAuthRepo";
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final GoogleSignInClient googleSignInClient;

    public AuthRepository() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(Constants.WEB_CLIENT_ID)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(AppContextProvider.getContext(), gso);}

    public void registerWithEmail(String email, String password, String displayName, String address,
                                  DataStateCallback<FirebaseUser> dataStateCallback) {

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        saveUserToFirestore(user, displayName, address);
                        dataStateCallback.onSuccess(user);
                    } else {
                        dataStateCallback.onError(Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
    }

    public void loginWithEmail(String email, String password, DataStateCallback<User> dataStateCallback) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && firebaseAuth.getCurrentUser() != null) {
                        String uid = firebaseAuth.getCurrentUser().getUid();
                        getUserInfoById(uid, dataStateCallback);
                    } else {
                        String errorMsg = Objects.requireNonNull(task.getException()).getMessage();
                        dataStateCallback.onError(errorMsg != null ? errorMsg : "Login failed!");
                    }
                });
    }

    public void loginWithCredential(AuthCredential credential,
                                    MutableLiveData<Resource<FirebaseUser>> userLiveData) {
        userLiveData.setValue(Resource.loading(null));

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        saveUserToFirestore(user, user.getDisplayName(), "");
                        userLiveData.setValue(Resource.success(user));
                    } else {
                        userLiveData.setValue(Resource.error(task.getException().getMessage(), null));
                    }
                });
    }

    private void saveUserToFirestore(FirebaseUser user, String displayName, String address) {
        if (user == null) return;

        User newUser = new User(
                user.getUid(),
                user.getEmail(),
                displayName,
                user.getPhoneNumber(),
                address,
                user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : "",
                Constants.Role.BUYER,
                Timestamp.now(),
                Timestamp.now()
        );

        firestore.collection(Constants.Collection.USERS)
                .document(user.getUid())
                .set(newUser)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "User added to Firestore"))
                .addOnFailureListener(e -> Log.e(TAG, "Error adding user", e));
    }

    public void getUserInfoById(String uuid, DataStateCallback<User> dataStateCallback) {
        firestore.collection(Constants.Collection.USERS)
                .document(uuid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    User user = documentSnapshot.toObject(User.class);
                    dataStateCallback.onSuccess(user);
                })
                .addOnFailureListener(ex -> {
                    dataStateCallback.onError(ex.getMessage());
                });
    }

    public void signOut(DataStateCallback<String> callback) {
        try {
            firebaseAuth.signOut();
            callback.onSuccess("Login successfully!");
        } catch (Exception e) {
            callback.onSuccess(e.getMessage());
        }
    }

    public void changePassword(String email, String password, String newPassword, DataStateCallback<String> callback) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) {
            return;
        }

        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        user.reauthenticate(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                user.updatePassword(newPassword).addOnCompleteListener(updateTask -> {
                    if (updateTask.isSuccessful()) {
                        callback.onSuccess("Đổi mật khẩu thành công");
                    } else {
                        callback.onError(Objects.requireNonNull(updateTask.getException()).getMessage());
                    }
                });
            } else {
                String errorMessage = Objects.requireNonNull(task.getException()).getMessage();
                if (task.getException() instanceof FirebaseAuthRecentLoginRequiredException) {
                    errorMessage = "Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại.";
                }
                callback.onError(errorMessage);
            }
        });
    }

    public Intent getGoogleSignInIntent() {
        return googleSignInClient.getSignInIntent();
    }

    public Task<FirebaseUser> handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult();

            if (account != null) {
                String idToken = account.getIdToken();
                AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

                // Sử dụng signInWithCredential và xử lý kết quả
                return FirebaseAuth.getInstance()
                        .signInWithCredential(credential)
                        .continueWithTask(task -> {
                            if (task.isSuccessful()) {
                                // Trả về Task chứa FirebaseUser
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                return Tasks.forResult(user);
                            } else {
                                // Ném ngoại lệ nếu đăng nhập thất bại
                                throw task.getException();
                            }
                        });
            } else {
                // Trả về Task thất bại nếu account == null
                return Tasks.forException(new Exception("Google Sign-In account is null"));
            }
        } catch (Exception e) {
            // Trả về Task thất bại nếu có lỗi
            return Tasks.forException(e);
        }
    }
}
