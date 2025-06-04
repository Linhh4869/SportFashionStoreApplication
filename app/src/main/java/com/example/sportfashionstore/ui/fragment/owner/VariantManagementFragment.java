package com.example.sportfashionstore.ui.fragment.owner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.sportfashionstore.R;
import com.example.sportfashionstore.commonbase.BaseBottomSheetFragment;
import com.example.sportfashionstore.databinding.FragmentCurdVariantBinding;
import com.example.sportfashionstore.model.ProductVariant;
import com.example.sportfashionstore.model.SizeModel;
import com.example.sportfashionstore.ui.adapter.SizeVariantAdapter;
import com.example.sportfashionstore.util.Constants;
import com.example.sportfashionstore.viewmodel.VariantManagementViewModel;

import java.util.List;

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
        setCancelable(false);
        if (variant != null) {
            binding.setVariant(variant);
            binding.btnPickImage.setVisibility(View.GONE);
            binding.imgPicked.setVisibility(View.VISIBLE);
            Glide.with(binding.getRoot().getContext())
                    .load(variant.getImage())
                    .placeholder(R.drawable.icon_clothes)
                    .error(R.drawable.coat0)
                    .into(binding.imgPicked);
        }

        if (getTag() != null && getTag().equals(Constants.ADD_VARIANT)) {
            binding.btnCurdVariant.setText("Thêm mẫu hàng hóa");
        } else if (getTag() != null && getTag().equals(Constants.EDIT_VARIANT)) {
            binding.btnCurdVariant.setText("Chỉnh sửa mẫu hàng hóa");
        }

        binding.btnCurdVariant.setOnClickListener(v -> {
            dismiss();
        });

        SizeVariantAdapter sizeVariantAdapter = new SizeVariantAdapter(item -> {

        });
        List<SizeModel> dataSize = variant != null ? viewModel.getAllSizeOfVariant(variant.getSize()) : viewModel.getAllSize();
        sizeVariantAdapter.setData(dataSize);
        binding.rcvSize.setAdapter(sizeVariantAdapter);
    }

    @Override
    protected void observerData() {

    }
}
