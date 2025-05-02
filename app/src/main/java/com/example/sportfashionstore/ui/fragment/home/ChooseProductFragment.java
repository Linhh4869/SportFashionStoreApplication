package com.example.sportfashionstore.ui.fragment.home;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.sportfashionstore.R;
import com.example.sportfashionstore.commonbase.BaseBottomSheetFragment;
import com.example.sportfashionstore.databinding.LayoutBottomSheetChooseProductBinding;
import com.example.sportfashionstore.model.Product;
import com.example.sportfashionstore.model.ProductVariant;
import com.example.sportfashionstore.viewmodel.ChooseProductViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;
import java.util.Objects;

public class ChooseProductFragment extends BaseBottomSheetFragment<LayoutBottomSheetChooseProductBinding> {
    private ChooseProductViewModel viewModel;
    private int value = 1;
    private int MAX_VALUE = 99;

    @Override
    protected LayoutBottomSheetChooseProductBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return LayoutBottomSheetChooseProductBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUi() {
        viewModel = new ViewModelProvider(requireActivity()).get(ChooseProductViewModel.class);
        Product product = Objects.requireNonNull(viewModel.getProductLiveData().getValue()).data;
        List<ProductVariant> productVariants = product.getProductVariants();
        MAX_VALUE = productVariants.get(0).getInventory();
        binding.tvPrice.setText(product.getDisplayPrice());
        int saleColor = ResourcesCompat.getColor(binding.getRoot().getResources(), R.color.sailing_coral_red, null);
        int notSaleColor = ResourcesCompat.getColor(binding.getRoot().getResources(), R.color.sailing_navy_blue, null);
        if (product.isSaleClothes()) {
            binding.tvPrice.setTextColor(saleColor);
            binding.tvSalePrice.setVisibility(View.VISIBLE);
            binding.tvSalePrice.setText(product.getDisplaySalePrice());
            binding.tvSalePrice.setPaintFlags(binding.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            binding.tvPrice.setTextColor(notSaleColor);
            binding.tvSalePrice.setVisibility(View.GONE);
        }

        updateQuantityStates();

        binding.btnDecrease.setOnClickListener(v -> {
            if (value > 1) {
                value--;
            }
            updateQuantityStates();
        });

        binding.btnIncrease.setOnClickListener(v -> {
            if (value < MAX_VALUE) {
                value++;
                binding.tvQuantity.setText(String.valueOf(value));
            }
            updateQuantityStates();
        });
    }

    private void updateQuantityStates() {
        binding.btnDecrease.setEnabled(value > 1);
        binding.btnIncrease.setEnabled(value < MAX_VALUE);
        binding.tvQuantity.setText(String.valueOf(value));
    }
}
