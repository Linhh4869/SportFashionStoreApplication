package com.example.sportfashionstore.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.sportfashionstore.R;
import com.example.sportfashionstore.commonbase.BaseAdapter;
import com.example.sportfashionstore.databinding.ItemOrderStatusBinding;
import com.example.sportfashionstore.model.Order;

public class OrderAdapter extends BaseAdapter<Order, ItemOrderStatusBinding> {
    @Override
    public void bind(ItemOrderStatusBinding binding, Order item, int position) {
        binding.setOrder(item);
//        binding.tvContentStatus.setText(item.getContentStatus().getDesc());
        binding.tvContentStatus.setTextColor(item.getContentStatus().getTextColor());

        Glide.with(binding.getRoot().getContext())
                .load(item.getImage())
                .placeholder(R.drawable.icon_clothes)
                .error(R.drawable.coat0)
                .into(binding.imgProduct);

        binding.tvSalePrice.setVisibility(item.isSaleClothes() ? View.VISIBLE : View.GONE);
    }

    @Override
    protected ItemOrderStatusBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return ItemOrderStatusBinding.inflate(inflater, container, false);
    }
}
