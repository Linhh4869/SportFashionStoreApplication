package com.example.sportfashionstore.ui;

import android.graphics.Paint;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.example.sportfashionstore.R;
import com.example.sportfashionstore.commonbase.BaseActivityViewModel;
import com.example.sportfashionstore.databinding.ActivityCheckoutBinding;
import com.example.sportfashionstore.ui.adapter.InfoPaymentAdapter;
import com.example.sportfashionstore.viewmodel.CheckoutViewModel;

public class CheckoutActivity extends BaseActivityViewModel<ActivityCheckoutBinding, CheckoutViewModel> {
    public static final String KEY_DATA = "CART_DATA";

    @Override
    protected void setupUi() {
        long id = 0;
        if (getIntent() != null && getIntent().getSerializableExtra(KEY_DATA) != null) {
            id = (long) getIntent().getSerializableExtra(KEY_DATA);
        }

        viewModel.getInfoPayment(id);
    }

    @Override
    protected void setupObservers() {
        viewModel.getCartEntityLiveData().observe(this, cart -> {
            if (cart == null) {
                return;
            }

            binding.setData(cart);
            binding.setInfo(viewModel);

            binding.tvPriceProduct.setText(cart.getDisplayPrice());
            int saleColor = ResourcesCompat.getColor(binding.getRoot().getResources(), R.color.sailing_coral_red, null);
            int notSaleColor = ResourcesCompat.getColor(binding.getRoot().getResources(), R.color.sailing_navy_blue, null);
            if (cart.isSalePrice()) {
                binding.tvPriceProduct.setTextColor(saleColor);
                binding.tvSalePrice.setVisibility(View.VISIBLE);
                binding.tvSalePrice.setText(cart.getDisplaySalePrice());
                binding.tvSalePrice.setPaintFlags(binding.tvPriceProduct.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                binding.tvPriceProduct.setTextColor(notSaleColor);
                binding.tvSalePrice.setVisibility(View.GONE);
            }

            Glide.with(binding.getRoot().getContext())
                    .load(cart.getImage())
                    .placeholder(R.drawable.icon_clothes)
                    .error(R.drawable.coat0)
                    .into(binding.imgProduct);

            int mPrice = (int) (cart.getSalePrice() > 0 ? cart.getSalePrice() : cart.getPrice());
            int allProductPrice = mPrice * cart.getQuantity();
            int totalPrice = allProductPrice + 15000;
            InfoPaymentAdapter infoPaymentAdapter = new InfoPaymentAdapter();
            infoPaymentAdapter.setData(viewModel.getInfoPaymentList(allProductPrice, totalPrice));
            binding.rcvPayment.setAdapter(infoPaymentAdapter);

            binding.btnCheckout.setOnClickListener(v -> {
                viewModel.clearData(cart.getId());
            });

            binding.btnBack.setOnClickListener(v -> {
                if (cart.isShowCart() == 0) {
                    viewModel.clearData(cart.getId());
                }

                onBackPressed();
            });
        });
    }
}
