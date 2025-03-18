package com.example.sportfashionstore.commonbase;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class BaseActivity<VB extends ViewDataBinding, VM extends BaseViewModel> extends AppCompatActivity {
    protected VB binding;
    protected VM viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, getLayoutResId());
        binding.setLifecycleOwner(this);
        setContentView(binding.getRoot());

        viewModel = createViewModel();

        setupObservers();
        setupUi();
    }

    protected abstract int getLayoutResId();
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

    protected void observeBaseViewModel() {
        if (viewModel != null) {
            viewModel.getLoading().observe(this, this::handleLoading);
            viewModel.getErrorMessage().observe(this, this::showToast);
        }
    }

    protected <T> void observeState(LiveData<Resource<T>> liveData, DataStateCallback<T> callback) {
        liveData.observe(this, resource -> {
            if (resource != null) {
                switch (resource.state) {
                    case LOADING:
                        handleLoading(true);
                        break;
                    case SUCCESS:
                        handleLoading(false);
                        callback.onSuccess(resource.data);
                        break;
                    case ERROR:
                        handleLoading(false);
                        showToast(resource.message);
                        callback.onError(resource.message);
                        break;
                }
            }
        });
    }

    private void handleLoading(boolean isLoading) {
        // Show/hide loading UI (if needed)
    }

    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
