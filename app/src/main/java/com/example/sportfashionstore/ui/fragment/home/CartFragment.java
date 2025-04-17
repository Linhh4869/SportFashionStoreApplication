package com.example.sportfashionstore.ui.fragment.home;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.sportfashionstore.commonbase.BaseFragment;
import com.example.sportfashionstore.commonbase.BaseFragmentViewModel;
import com.example.sportfashionstore.databinding.FragmentCartBinding;
import com.example.sportfashionstore.viewmodel.AuthViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class CartFragment extends BaseFragmentViewModel<FragmentCartBinding, AuthViewModel> {
    private static final int RC_SIGN_IN = 9001;
    private ActivityResultLauncher<Intent> googleSignInLauncher;

    @Override
    protected FragmentCartBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentCartBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUi() {
        googleSignInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                        Intent data = result.getData();
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//                        viewModel.handleGoogleSignInResult(task);
                        handleResult(task);
                }
        );

        binding.textView.setOnClickListener(v ->
                startGoogleSignIn()
        );
    }

    private void startGoogleSignIn() {
        Intent signInIntent = viewModel.getGoogleSignInIntent();
        googleSignInLauncher.launch(signInIntent);
    }

    private void handleResult(Task<GoogleSignInAccount> completedTask) {
        try {
            completedTask.addOnCompleteListener(taskResult -> {
                if (taskResult.isSuccessful()) {
                    GoogleSignInAccount account = completedTask.getResult();
                    String idToken = account.getIdToken();
                    Log.d("idToken", "idToken: " + idToken);

                } else {
                    Exception exception = taskResult.getException();
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        int statusCode = apiException.getStatusCode();
                        String errorMessage = apiException.getMessage();
                        String name = apiException.getStatusMessage();
                        Log.e("GoogleSignInError", "StatusCode: " + statusCode + ", Message: " + errorMessage + ", Name: " + name);
                    } else if (exception != null) {
                        Log.e("GoogleSignInError", "Lỗi không xác định: " + exception.getMessage());
                    }
                }
            });

        } catch (Exception e) {
            Log.d("idToken", "idToken: " + e.getMessage());
        }
    }
}
