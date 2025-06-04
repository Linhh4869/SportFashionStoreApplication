package com.example.sportfashionstore.ui.fragment.owner;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;

import com.example.sportfashionstore.commonbase.BaseBottomSheetFragment;
import com.example.sportfashionstore.databinding.FragmentCurdVariantBinding;
import com.example.sportfashionstore.model.ProductVariant;
import com.example.sportfashionstore.util.Constants;
import com.example.sportfashionstore.viewmodel.VariantManagementViewModel;

public class VariantManagementFragment extends BaseBottomSheetFragment<FragmentCurdVariantBinding, VariantManagementViewModel> {
    private ProductVariant variant;

    public void setVariant(ProductVariant variant) {
        this.variant = variant;
    }
    @Override
    protected FragmentCurdVariantBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentCurdVariantBinding.inflate(inflater, container, false);
    }

    @Override
    protected VariantManagementViewModel getViewModel() {
        return new ViewModelProvider(requireActivity()).get(VariantManagementViewModel.class);
    }

    @Override
    protected void initView() {
        if (variant != null) {
            binding.setVariant(variant);
        }

        if (getTag() != null && getTag().equals(Constants.ADD_VARIANT)) {
            binding.btnCurdVariant.setText("Thêm mẫu hàng hóa");
        } else if (getTag() != null && getTag().equals(Constants.EDIT_VARIANT)) {
            binding.btnCurdVariant.setText("Chỉnh sửa mẫu hàng hóa");
        }

        binding.btnCurdVariant.setOnClickListener(v -> {
            dismiss();
        });
    }

    @Override
    protected void observerData() {

    }
}
