package com.example.sportfashionstore.ui.fragment.home;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.sportfashionstore.R;
import com.example.sportfashionstore.callback.OnItemClickListener;
import com.example.sportfashionstore.commonbase.BaseBottomSheetFragment;
import com.example.sportfashionstore.databinding.LayoutBottomSheetChooseProductBinding;
import com.example.sportfashionstore.model.Product;
import com.example.sportfashionstore.model.ProductVariant;
import com.example.sportfashionstore.ui.adapter.ColorAdapter;
import com.example.sportfashionstore.ui.adapter.SizeAdapter;
import com.example.sportfashionstore.util.Constants;
import com.example.sportfashionstore.viewmodel.ChooseProductViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;
import java.util.Objects;

public class ChooseProductFragment extends BaseBottomSheetFragment<LayoutBottomSheetChooseProductBinding> {
    private ChooseProductViewModel viewModel;
    private SizeAdapter sizeAdapter;
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
        MAX_VALUE = Integer.parseInt(productVariants.get(0).getInventory());
        
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

        if (getTag() != null && getTag().equals(Constants.PAY_NOW)) {
            binding.btnPay.setText("Thanh toán");
        } else if (getTag() != null && getTag().equals(Constants.ADD_CART)) {
            binding.btnPay.setText("Thêm vào giỏ hàng");
        }

        updateInfoSelectedVariant(productVariants.get(0));
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

        binding.btnClose.setOnClickListener(v -> {
            dismiss();
        });

        ColorAdapter colorAdapter = new ColorAdapter(item -> {
            updateInfoSelectedVariant(item);
            if (sizeAdapter != null) {
                sizeAdapter.setData(viewModel.setStateSize(item));
                sizeAdapter.resetState();
            }
        });
        colorAdapter.setData(productVariants);
        binding.rcvColor.setAdapter(colorAdapter);

        sizeAdapter = new SizeAdapter(item -> {

        });
        sizeAdapter.setData(viewModel.setStateSize(productVariants.get(0)));
        binding.rcvSize.setAdapter(sizeAdapter);
    }
    
    private void updateInfoSelectedVariant(ProductVariant variant) {
        Glide.with(binding.getRoot().getContext())
                .load(variant.getImage())
                .placeholder(R.drawable.icon_clothes)
                .error(R.drawable.coat0)
                .into(binding.imgProduct);

        binding.tvInventory.setText(variant.getInvText());
        MAX_VALUE = Integer.parseInt(variant.getInventory());
    }

    private void updateQuantityStates() {
        binding.btnDecrease.setEnabled(value > 1);
        binding.btnIncrease.setEnabled(value < MAX_VALUE);
        binding.tvQuantity.setText(String.valueOf(value));
    }
}
