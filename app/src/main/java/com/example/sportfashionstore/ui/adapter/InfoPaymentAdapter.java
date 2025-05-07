package com.example.sportfashionstore.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.sportfashionstore.commonbase.BaseAdapter;
import com.example.sportfashionstore.databinding.ItemInfoPaymentBinding;
import com.example.sportfashionstore.model.InfoPayment;
import com.example.sportfashionstore.util.Helper;

public class InfoPaymentAdapter extends BaseAdapter<InfoPayment, ItemInfoPaymentBinding> {
    @Override
    public void bind(ItemInfoPaymentBinding binding, InfoPayment item, int position) {
        binding.tvLabel.setText(item.getLabel());
        String price = String.format("%sđ", Helper.formatPrice(item.getValue()));
        binding.tvValue.setText(price);
    }

    @Override
    protected ItemInfoPaymentBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return ItemInfoPaymentBinding.inflate(inflater, container, false);
    }
}
