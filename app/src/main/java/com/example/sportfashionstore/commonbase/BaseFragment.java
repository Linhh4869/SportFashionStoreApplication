package com.example.sportfashionstore.commonbase;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class BaseFragment<VB extends ViewDataBinding, VM extends BaseViewModel> extends Fragment {
    protected VB binding;
    protected VM viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayoutResId(), container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        throw new IllegalStateException("BaseFragment requires a generic ViewModel type.");
    }

    protected void observeBaseViewModel() {
        if (viewModel != null) {
            viewModel.getLoading().observe(getViewLifecycleOwner(), this::handleLoading);
            viewModel.getErrorMessage().observe(getViewLifecycleOwner(), this::showToast);
        }
    }

    protected <T> void observeState(LiveData<Resource<T>> liveData, DataStateCallback<T> callback) {
        liveData.observe(getViewLifecycleOwner(), resource -> {
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
        // Hiển thị hoặc ẩn UI loading nếu cần
    }

    protected void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }


}
