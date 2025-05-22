package com.example.sportfashionstore.ui.adapter;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.core.content.res.ResourcesCompat;

import com.example.sportfashionstore.R;
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
        if (getItemCount() > 0 && position == getItemCount() - 1) {
            Typeface typeface = ResourcesCompat.getFont(binding.getRoot().getContext(), R.font.sarabun_bold);
            binding.tvLabel.setTypeface(typeface);
            binding.tvValue.setTypeface(typeface);
        }
    }

    @Override
    protected ItemInfoPaymentBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return ItemInfoPaymentBinding.inflate(inflater, container, false);
    }
}
