package com.example.sportfashionstore.ui.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.example.sportfashionstore.R;
import com.example.sportfashionstore.commonbase.BaseAdapter;
import com.example.sportfashionstore.data.entity.CartEntity;
import com.example.sportfashionstore.databinding.ItemCartBinding;

public class CartAdapter extends BaseAdapter<CartEntity, ItemCartBinding> {
    private final ItemCartListener listener;

    public CartAdapter(ItemCartListener listener) {
        this.listener = listener;
    }

    @Override
    public void bind(ItemCartBinding binding, CartEntity item, int position) {
        binding.setCartData(item);
        Glide.with(binding.getRoot().getContext())
                .load(item.getImage())
                .placeholder(R.drawable.icon_clothes)
                .error(R.drawable.coat0)
                .into(binding.imgProduct);

        binding.tvPriceProduct.setText(item.getDisplayPrice());
        int saleColor = ResourcesCompat.getColor(binding.getRoot().getResources(), R.color.sailing_coral_red, null);
        int notSaleColor = ResourcesCompat.getColor(binding.getRoot().getResources(), R.color.sailing_navy_blue, null);
        if (item.isSalePrice()) {
            binding.tvPriceProduct.setTextColor(saleColor);
            binding.tvSalePrice.setVisibility(View.VISIBLE);
            binding.tvSalePrice.setText(item.getDisplaySalePrice());
            binding.tvSalePrice.setPaintFlags(binding.tvPriceProduct.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            binding.tvPriceProduct.setTextColor(notSaleColor);
            binding.tvSalePrice.setVisibility(View.GONE);
        }

        binding.btnCheckout.setOnClickListener(v -> {
            listener.onPayItemCart(item);
        });

        binding.btnRemove.setOnClickListener(v -> {
            listener.onDeleteItemCart(item);
        });
    }

    @Override
    protected ItemCartBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return ItemCartBinding.inflate(inflater, container, false);
    }

    public interface ItemCartListener {
        void onDeleteItemCart(CartEntity cart);

        void onPayItemCart(CartEntity cartEntity);
    }
}
