package com.example.sportfashionstore;

import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;

import com.example.sportfashionstore.commonbase.BaseActivity;
import com.example.sportfashionstore.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity<ActivityMainBinding> {
    public static final String SHOW_STANDBY = "show_standby";

    private boolean showStandby = true;
    private boolean shouldExitApp = false;

    @Override
    protected void setupUi() {
        showStandby = getIntent().getBooleanExtra(SHOW_STANDBY, true);
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (shouldExitApp) {
                    finish();
                } else {
                    shouldExitApp = true;
                    Toast.makeText(MainActivity.this, "Nhấn lần nữa để thoát", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean isShowStandby() {
        return showStandby;
    }
}