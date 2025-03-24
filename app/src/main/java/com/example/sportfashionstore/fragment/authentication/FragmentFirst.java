package com.example.sportfashionstore.fragment.authentication;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.sportfashionstore.activity.MainActivity;
import com.example.sportfashionstore.R;
import com.example.sportfashionstore.activity.ActivityHome;
import com.example.sportfashionstore.commonbase.BaseFragment;
import com.example.sportfashionstore.databinding.FragmentFirstBinding;
import com.example.sportfashionstore.util.SharePrefHelper;

public class FragmentFirst extends BaseFragment<FragmentFirstBinding> implements Animation.AnimationListener {

    @Override
    protected FragmentFirstBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentFirstBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUI() {
        Animation mAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.custom_bounce);
        mAnimation.setAnimationListener(this);
        binding.icApp.setAnimation(mAnimation);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        binding.icApp.setAnimation(null);
        SharePrefHelper sharePrefHelper = new SharePrefHelper(requireContext());
        if (sharePrefHelper.isLoggedIn()) {
            Intent intent = new Intent(getActivity(), ActivityHome.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            requireActivity().startActivity(intent);
            requireActivity().finish();
        } else {
            MainActivity mActivity = (MainActivity) getActivity();
            boolean showStandby = true;
            if (mActivity != null) {
                showStandby = mActivity.isShowStandby();
            }

            int destination = showStandby ? R.id.action_to_standby : R.id.action_to_auth;
            NavController navController = Navigation.findNavController(binding.getRoot());
            navController.navigate(destination);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
