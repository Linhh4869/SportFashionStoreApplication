package com.example.sportfashionstore.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.sportfashionstore.R;
import com.example.sportfashionstore.commonbase.BaseActivity;
import com.example.sportfashionstore.databinding.ActivityHomeBinding;

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

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.container);
        NavController navController = navHostFragment.getNavController();
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {

            }
        });

        NavigationUI.setupWithNavController(binding.bottomNavigation, navController);

    }
}
