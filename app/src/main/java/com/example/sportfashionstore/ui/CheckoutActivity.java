package com.example.sportfashionstore.ui;

import android.content.Intent;
import android.graphics.Paint;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.example.sportfashionstore.R;
import com.example.sportfashionstore.commonbase.BaseActivityViewModel;
import com.example.sportfashionstore.commonbase.Resource;
import com.example.sportfashionstore.data.entity.CartEntity;
import com.example.sportfashionstore.databinding.ActivityCheckoutBinding;
import com.example.sportfashionstore.ui.adapter.InfoPaymentAdapter;
import com.example.sportfashionstore.util.Helper;
import com.example.sportfashionstore.viewmodel.CheckoutViewModel;

public class CheckoutActivity extends BaseActivityViewModel<ActivityCheckoutBinding, CheckoutViewModel> {
    public static final String KEY_DATA = "CART_DATA";
    private CartEntity mCart = null;

    @Override
    protected void setupUi() {
        long id = getIntent().getLongExtra(KEY_DATA, -1);
        viewModel.getInfoPayment(id);
    }

    @Override
    protected void setupObservers() {
        viewModel.getCartEntityLiveData().observe(this, cart -> {
            if (cart == null) {
                return;
            }

            mCart = cart;
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
            binding.tvTotalCost.setText(String.format("%sđ", Helper.formatPrice(totalPrice)));
            InfoPaymentAdapter infoPaymentAdapter = new InfoPaymentAdapter();
            infoPaymentAdapter.setData(viewModel.getInfoPaymentList(allProductPrice, totalPrice));
            binding.rcvPayment.setAdapter(infoPaymentAdapter);

            binding.btnCheckout.setOnClickListener(v -> {
                viewModel.saveOrder(cart);
            });

            binding.btnBack.setOnClickListener(v -> {
                onBackPressed();
            });
        });

        viewModel.getEnablePayButton().observe(this, isEnable -> {
            binding.btnCheckout.setEnabled(isEnable);
        });

        viewModel.getSaveOrderLiveData().observe(this, resource -> {
            if (resource.state.equals(Resource.State.SUCCESS) && resource.data != null) {
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mCart != null && mCart.isShowCart() == 0) {
            viewModel.clearData(mCart.getId());
        }
    }
}
