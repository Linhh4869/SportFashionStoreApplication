package com.example.sportfashionstore.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.sportfashionstore.R;
import com.example.sportfashionstore.commonbase.BaseAdapter;
import com.example.sportfashionstore.databinding.ItemCommonImageBinding;

public class CommonImageAdapter extends BaseAdapter<String, ItemCommonImageBinding> {

    @Override
    public void bind(ItemCommonImageBinding binding, String item, int position) {
        Glide.with(binding.getRoot().getContext())
                .load(item)
                .placeholder(R.drawable.icon_clothes)
                .error(R.drawable.coat0)
                .into(binding.imgClothes);
    }

    @Override
    protected ItemCommonImageBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return ItemCommonImageBinding.inflate(inflater, container, false);
    }
}
