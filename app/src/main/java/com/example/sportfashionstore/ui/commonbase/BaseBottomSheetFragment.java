package com.example.sportfashionstore.ui.commonbase;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.ViewDataBinding;

import com.example.sportfashionstore.R;
import com.example.sportfashionstore.custom.LoadingDialog;
import com.example.sportfashionstore.util.Helper;
import com.example.sportfashionstore.util.StringUtil;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public abstract class BaseBottomSheetFragment<VB extends ViewDataBinding, VM extends BaseViewModel> extends BottomSheetDialogFragment {
    protected VB binding;
    protected VM viewModel;
    private LoadingDialog loadingDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = getViewBinding(inflater, container);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        viewModel = getViewModel();
        loadingDialog = new LoadingDialog(requireContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        observeBaseViewModel();
        observerData();
        setupKeyboardHandling(view);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(dialogInterface -> {
            BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
            View bottomSheet = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setFitToContents(true);
                behavior.setSkipCollapsed(true);
                bottomSheet.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.bottom_sheet_slide_up));
            }
        });

        return dialog;
    }

    private void setupKeyboardHandling(View view) {
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
            int keyboardHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom;
            BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View) v.getParent());
            behavior.setPeekHeight(keyboardHeight + v.getHeight());
            return insets;
        });
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

    private void handleLoading(boolean isLoading) {
        if (isLoading) {
            loadingDialog.show();
        } else {
            loadingDialog.dismiss();
        }
    }

    protected void showToast(String message) {
        Helper.showMyToast(getActivity(), message);
    }

    protected abstract VB getViewBinding(LayoutInflater inflater, ViewGroup container);
    protected abstract VM getViewModel();
    protected abstract void initView();
    protected abstract void observerData();
}
