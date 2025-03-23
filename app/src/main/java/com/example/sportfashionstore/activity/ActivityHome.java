package com.example.sportfashionstore.activity;

import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;

import com.example.sportfashionstore.MainActivity;
import com.example.sportfashionstore.commonbase.BaseActivity;
import com.example.sportfashionstore.databinding.ActivityHomeBinding;
import com.example.sportfashionstore.util.SharePrefHelper;

public class ActivityHome extends BaseActivity<ActivityHomeBinding> {
    private boolean shouldExitApp = false;

    @Override
    protected void setupUi() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (shouldExitApp) {
                    finish();
                } else {
                    shouldExitApp = true;
                    Toast.makeText(ActivityHome.this, "Nhấn lần nữa để thoát", Toast.LENGTH_SHORT).show();
                }
            }
        });

        SharePrefHelper sharePrefHelper = new SharePrefHelper(this);
        String name = sharePrefHelper.getUserName();
        String address = sharePrefHelper.getAddress();
        if (!name.isEmpty() && !address.isEmpty()) {
            String hello = "Hello " + name + " from " + address;
            binding.tvTest.setText(hello);
        }
    }
}
