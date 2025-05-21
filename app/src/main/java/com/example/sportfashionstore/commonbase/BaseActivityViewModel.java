package com.example.sportfashionstore.commonbase;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.sportfashionstore.callback.DataStateCallback;
import com.example.sportfashionstore.custom.LoadingDialog;
import com.example.sportfashionstore.util.Helper;
import com.example.sportfashionstore.util.StringUtil;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class BaseActivityViewModel<VB extends ViewDataBinding, VM extends BaseViewModel> extends AppCompatActivity {
    protected VB binding;
    protected VM viewModel;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = getViewBinding();
        binding.setLifecycleOwner(this);
        setContentView(binding.getRoot());

        viewModel = createViewModel();
        loadingDialog = new LoadingDialog(this);

        setupObservers();
        setTransparentStatusBar();
        setupUi();
        observeBaseViewModel();
    }

    protected abstract void setupUi();
    protected abstract void setupObservers();

    @SuppressWarnings("unchecked")
    private VM createViewModel() {
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Class<VM> viewModelClass = (Class<VM>) ((ParameterizedType) type).getActualTypeArguments()[1];
            return new ViewModelProvider(this).get(viewModelClass);
        }
        throw new IllegalStateException("BaseActivity requires a generic ViewModel type.");
    }

    private VB getViewBinding() {
        try {
            Type superclass = getClass().getGenericSuperclass();
            Class<VB> bindingClass = (Class<VB>) ((ParameterizedType) superclass).getActualTypeArguments()[0];

            Method inflateMethod = bindingClass.getMethod("inflate", android.view.LayoutInflater.class);
            return (VB) inflateMethod.invoke(null, getLayoutInflater());
        } catch (Exception e) {
            throw new RuntimeException("Error creating ViewBinding", e);
        }
    }

    protected void observeBaseViewModel() {
        if (viewModel != null) {
            if (loadingDialog != null) {
                viewModel.getLoading().observe(this, this::handleLoading);
            }

            viewModel.getErrorMessage().observe(this, message -> {
                if (StringUtil.isNotNullAndEmpty(message)) {
                    showToast(message);
                }
            });
        }
    }

    private void handleLoading(boolean isLoading) {
        if (isLoading) {
            loadingDialog.show();
        } else {
            loadingDialog.dismiss();
        }
    }

    protected void showToast(String message) {
        Helper.showMyToast(this, message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void setTransparentStatusBar() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        Window win = this.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        winParams.flags &= ~WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        win.setAttributes(winParams);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }
}
