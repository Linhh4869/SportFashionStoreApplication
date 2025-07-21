package com.example.sportfashionstore.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import com.example.sportfashionstore.R;
import com.example.sportfashionstore.databinding.DialogConfirmBinding;

public class CommonConfirmDialog {

    public interface OnConfirmListener {
        void onConfirm();
    }

    private final Context context;
    private String content = "";
    private boolean showCancelButton = true;
    private OnConfirmListener confirmListener;
    private boolean cancelable = true;

    public CommonConfirmDialog(Context context) {
        this.context = context;
    }

    public CommonConfirmDialog setContent(String content) {
        this.content = content;
        return this;
    }

    public CommonConfirmDialog showCancelButton(boolean show) {
        this.showCancelButton = show;
        return this;
    }

    public CommonConfirmDialog setOnConfirmListener(OnConfirmListener listener) {
        this.confirmListener = listener;
        return this;
    }

    public CommonConfirmDialog setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }

    public Dialog create() {
        Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        DialogConfirmBinding binding = DialogConfirmBinding.inflate(LayoutInflater.from(context));
        dialog.setContentView(binding.getRoot());

        binding.tvContent.setText(content);

        binding.btnCancel.setVisibility(showCancelButton ? android.view.View.VISIBLE : android.view.View.GONE);

        binding.btnClose.setOnClickListener(v -> dialog.dismiss());

        binding.btnCancel.setOnClickListener(v -> dialog.dismiss());

        binding.btnConfirm.setOnClickListener(v -> {
            if (confirmListener != null) {
                confirmListener.onConfirm();
            }
            dialog.dismiss();
        });

        // Set dialog properties
        dialog.setCancelable(cancelable);

        // Set window properties
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }

        return dialog;
    }

    public Dialog show() {
        Dialog dialog = create();
        dialog.show();
        return dialog;
    }
}
