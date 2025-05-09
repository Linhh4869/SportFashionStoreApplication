package com.example.sportfashionstore.commonbase;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

import com.example.sportfashionstore.R;

public abstract class BaseDialog<VB extends ViewDataBinding, VM extends BaseViewModel> extends Dialog {
    protected VB binding;
    protected VM viewModel;
    protected InputMethodManager inputManager;

    public BaseDialog(@NonNull Context context) {
        super(context, R.style.Theme_Dialog);
        this.inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    protected abstract VB getViewBinding(LayoutInflater inflater);
    protected abstract VM getViewModel();
    protected abstract void initView();
    protected abstract void setupListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getWindow() != null) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }

        binding = getViewBinding(LayoutInflater.from(getContext()));
        viewModel = getViewModel();
        setContentView(binding.getRoot());

        initView();
        setupListener();
    }

    public void showKeyboard() {
        if (binding.getRoot().getWindowToken() != null) {
            inputManager.showSoftInput(binding.getRoot(), InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public void hideKeyboard() {
        if (binding.getRoot().getWindowToken() != null) {
            inputManager.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
        }
    }

    public void closeDialog() {
        hideKeyboard();
        dismiss();
    }

    public void setCancelableDialog(boolean cancelable) {
        setCancelable(cancelable);
    }

    @Override
    public void show() {
        super.show();
        binding.getRoot().setAlpha(0f);
        binding.getRoot().animate().alpha(1f).setDuration(300).start();
    }

    @Override
    public void dismiss() {
        binding.getRoot().animate().alpha(0f).setDuration(300).withEndAction(super::dismiss).start();
    }
}
