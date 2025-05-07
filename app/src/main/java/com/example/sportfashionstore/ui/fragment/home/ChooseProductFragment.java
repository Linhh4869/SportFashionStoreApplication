package com.example.sportfashionstore.ui.fragment.home;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.sportfashionstore.R;
import com.example.sportfashionstore.commonbase.BaseBottomSheetFragment;
import com.example.sportfashionstore.databinding.LayoutBottomSheetChooseProductBinding;
import com.example.sportfashionstore.model.Product;
import com.example.sportfashionstore.model.ProductVariant;
import com.example.sportfashionstore.ui.CheckoutActivity;
import com.example.sportfashionstore.ui.adapter.ColorAdapter;
import com.example.sportfashionstore.ui.adapter.SizeAdapter;
import com.example.sportfashionstore.util.Constants;
import com.example.sportfashionstore.viewmodel.ChooseProductViewModel;

import java.util.List;
import java.util.Objects;

public class ChooseProductFragment extends BaseBottomSheetFragment<LayoutBottomSheetChooseProductBinding> {
    private ChooseProductViewModel viewModel;
    private SizeAdapter sizeAdapter;

    @Override
    protected LayoutBottomSheetChooseProductBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return LayoutBottomSheetChooseProductBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUi() {
        viewModel = new ViewModelProvider(requireActivity()).get(ChooseProductViewModel.class);
        Product product = Objects.requireNonNull(viewModel.getProductLiveData().getValue()).data;
        List<ProductVariant> productVariants = product.getProductVariants();

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

        binding.btnDecrease.setOnClickListener(v -> {
            viewModel.decreaseQuantity();
        });

        binding.btnIncrease.setOnClickListener(v -> {
            viewModel.increaseQuantity();
        });

        binding.btnClose.setOnClickListener(v -> {
            dismiss();
        });

        binding.btnPay.setOnClickListener(v -> {
            dismiss();
            viewModel.handleButton(getTag());
        });

        ColorAdapter colorAdapter = new ColorAdapter(item -> {
            viewModel.setCurrentVariant(item);
            if (sizeAdapter != null) {
                sizeAdapter.setData(viewModel.setStateSize(item));
                sizeAdapter.resetState();
            }
        });
        colorAdapter.setData(productVariants);
        binding.rcvColor.setAdapter(colorAdapter);

        sizeAdapter = new SizeAdapter(item -> {
            viewModel.setSelectedSize(item.getSize());
        });
        sizeAdapter.setData(viewModel.setStateSize(productVariants.get(0)));
        binding.rcvSize.setAdapter(sizeAdapter);

        viewModel.getQuantity().observe(getViewLifecycleOwner(), quantity -> {
            binding.tvQuantity.setText(String.valueOf(quantity));
        });

        viewModel.getCurrentVariant().observe(getViewLifecycleOwner(), variant -> {
            Glide.with(binding.getRoot().getContext())
                    .load(variant.getImage())
                    .placeholder(R.drawable.icon_clothes)
                    .error(R.drawable.coat0)
                    .into(binding.imgProduct);

            binding.tvInventory.setText(variant.getInvText());
            viewModel.setMAX_VALUE(Integer.parseInt(variant.getInventory()));
        });

        viewModel.onNavigateToCheckout().observe(getViewLifecycleOwner(), cartEntity -> {
            Intent intent = new Intent(getActivity(), CheckoutActivity.class);
            intent.putExtra(CheckoutActivity.KEY_DATA, cartEntity.getId());

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                requireActivity().startActivity(intent);
                }, 500);
        });

        viewModel.addToCartSuccess().observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        });
    }
}
