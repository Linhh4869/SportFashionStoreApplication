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
import androidx.lifecycle.ViewModelProvider;

import com.example.sportfashionstore.custom.LoadingDialog;
import com.example.sportfashionstore.util.StringUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class BaseFragmentViewModel<VB extends ViewDataBinding, VM extends BaseViewModel> extends Fragment {
    protected VB binding;
    protected VM viewModel;
    private LoadingDialog loadingDialog;

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
        loadingDialog = new LoadingDialog(requireContext());

        observeBaseViewModel();
        setupUi();
    }

    protected abstract int getLayoutResId();

    protected abstract void setupUi();

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
            if (loadingDialog != null) {
                viewModel.getLoading().observe(getViewLifecycleOwner(), this::handleLoading);
            }

            viewModel.getErrorMessage().observe(getViewLifecycleOwner(), message -> {
                if (StringUtil.isNotNullAndEmpty(message)) {
                    showToast(message);
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void handleLoading(boolean isLoading) {
        if (isLoading) {
            loadingDialog.show();
        } else {
            loadingDialog.dismiss();
        }
    }

    protected void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}
