package com.example.sportfashionstore.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.example.sportfashionstore.R;
import com.example.sportfashionstore.callback.OnItemClickListener;
import com.example.sportfashionstore.commonbase.BaseAdapter;
import com.example.sportfashionstore.databinding.ItemChooseClothesBinding;
import com.example.sportfashionstore.model.ProductVariant;

public class ColorAdapter extends BaseAdapter<ProductVariant, ItemChooseClothesBinding> {
    private final OnItemClickListener<ProductVariant> listener;
    private int selectedPosition = 0;

    public ColorAdapter(OnItemClickListener<ProductVariant> listener) {
        this.listener = listener;
    }

    @Override
    public void bind(ItemChooseClothesBinding binding, ProductVariant item, int position) {
        binding.tvLabel.setText(item.getDesc());

        Glide.with(binding.getRoot().getContext())
                .load(item.getImage())
                .placeholder(R.drawable.icon_clothes)
                .error(R.drawable.coat0)
                .into(binding.imgProduct);

        binding.getRoot().setOnClickListener(v -> {
            listener.onItemClicked(item);
            if (selectedPosition != position) {
                selectedPosition = position;
                notifyDataSetChanged();
            }
        });

        binding.container.setSelected(position == selectedPosition);
        int color = ResourcesCompat.getColor(binding.getRoot().getContext().getResources(), R.color.model_black, null);
        int selectedColor = ResourcesCompat.getColor(binding.getRoot().getContext().getResources(), R.color.ocean_blue, null);
        binding.tvLabel.setTextColor(position == selectedPosition ? selectedColor : color);
    }

    @Override
    protected ItemChooseClothesBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return ItemChooseClothesBinding.inflate(inflater, container, false);
    }
}
