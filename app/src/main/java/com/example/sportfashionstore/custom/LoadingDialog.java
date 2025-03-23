package com.example.sportfashionstore.custom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Window;

import androidx.annotation.NonNull;

import com.example.sportfashionstore.R;

public class LoadingDialog extends Dialog {
    public LoadingDialog(@NonNull Context context) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_loading_dialog);

        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        setCancelable(false);
    }

    @Override
    public void show() {
        Log.d("loading", "showing");
        if (!isShowing()) {
            super.show();
        }
    }

    @Override
    public void dismiss() {
        Log.d("loading", "dimiss");
        if (isShowing()) {
            super.dismiss();
        }
    }
}
