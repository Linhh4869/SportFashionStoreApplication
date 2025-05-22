package com.example.sportfashionstore.ui;

import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.sportfashionstore.R;
import com.example.sportfashionstore.app.MyApplication;
import com.example.sportfashionstore.commonbase.BaseActivity;
import com.example.sportfashionstore.databinding.ActivityHomeBinding;
import com.example.sportfashionstore.util.Constants;
import com.example.sportfashionstore.util.SharePrefHelper;

public class HomeActivity extends BaseActivity<ActivityHomeBinding> {
    private boolean shouldExitApp = false;
    public static String KEY_SCREEN = "key_screen";

    @Override
    protected void setupUi() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (shouldExitApp) {
                    finish();
                } else {
                    shouldExitApp = true;
                    Toast.makeText(HomeActivity.this, "Nhấn lần nữa để thoát", Toast.LENGTH_SHORT).show();
                }
            }
        });

        SharePrefHelper sharePrefHelper = MyApplication.getSharePrefHelper();
        String currentRole = sharePrefHelper.getRole();

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.container);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController);
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                navController.popBackStack(R.id.nav_home, false);
                navController.navigate(R.id.nav_home);
                return true;
            }

            return NavigationUI.onNavDestinationSelected(item, navController);
        });

        NavInflater navInflater = navController.getNavInflater();
        NavGraph navGraph;
        int menuId;

        switch (currentRole) {
            case Constants.Role.OWNER:
                navGraph = navInflater.inflate(R.navigation.nav_graph_owner_store);
                menuId = R.menu.bottom_nav_owner;
                break;
            case Constants.Role.SHIPPER:
                navGraph = navInflater.inflate(R.navigation.nav_graph_shipper);
                menuId = R.menu.bottom_nav_shipper;
                break;
            default:
                navGraph = navInflater.inflate(R.navigation.nav_graph_home);
                menuId = R.menu.bottom_nav_menu;
                break;
        }

        navController.setGraph(navGraph);
        binding.bottomNavigation.getMenu().clear();
        binding.bottomNavigation.inflateMenu(menuId);

        if (getIntent().getStringExtra(KEY_SCREEN) != null && currentRole.equals(Constants.Role.BUYER)) {
            try {
                String screen = getIntent().getStringExtra(KEY_SCREEN);
                if (screen == null || screen.isEmpty())
                    return;

                switch (screen) {
                    case "carts":
                        navController.navigate(R.id.nav_cart);
                        break;
                    case "orders":
                        navController.navigate(R.id.nav_order);
                        break;
                    default:
                        navController.navigate(R.id.nav_home);
                        break;
                }
            } catch (Exception ignored) {

            }
        }
    }
}
