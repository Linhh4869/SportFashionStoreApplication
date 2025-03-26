package com.example.sportfashionstore.ui.fragment.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.sportfashionstore.commonbase.BaseFragment;
import com.example.sportfashionstore.commonbase.BaseFragmentViewModel;
import com.example.sportfashionstore.databinding.FragmentProfileBinding;
import com.example.sportfashionstore.viewmodel.PersonalViewModel;

public class FragmentPersonal extends BaseFragmentViewModel<FragmentProfileBinding, PersonalViewModel> {
    @Override
    protected FragmentProfileBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentProfileBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUi() {

    }
}
