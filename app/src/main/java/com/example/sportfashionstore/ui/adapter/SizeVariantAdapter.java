package com.example.sportfashionstore.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.sportfashionstore.callback.OnItemClickListener;
import com.example.sportfashionstore.commonbase.BaseAdapter;
import com.example.sportfashionstore.databinding.ItemSizeBinding;
import com.example.sportfashionstore.model.SizeModel;

public class SizeVariantAdapter extends BaseAdapter<SizeModel, ItemSizeBinding> {
    private final OnItemClickListener<SizeModel> listener;

    public SizeVariantAdapter(OnItemClickListener<SizeModel> listener) {
        this.listener = listener;
    }
    @Override
    public void bind(ItemSizeBinding binding, SizeModel item, int position) {
        binding.tvSize.setText(item.getSize());
        binding.getRoot().setSelected(item.isSelected());
        binding.getRoot().setOnClickListener(v -> {
            item.setSelected(!item.isSelected());
            item.setPosition(position);
            listener.onItemClicked(item);
        });
    }

    @Override
    protected ItemSizeBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return ItemSizeBinding.inflate(inflater, container, false);
    }
}
