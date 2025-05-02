package com.example.sportfashionstore.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.sportfashionstore.R;
import com.example.sportfashionstore.callback.OnItemClickListener;
import com.example.sportfashionstore.commonbase.BaseAdapter;
import com.example.sportfashionstore.databinding.ItemChoosingClothesBinding;

public class ChoosingImageAdapter extends BaseAdapter<String, ItemChoosingClothesBinding> {
    private final OnItemClickListener<Integer> listener;
    private int selectedPosition = 0;

    public ChoosingImageAdapter(OnItemClickListener<Integer> listener) {
        this.listener = listener;
    }

    public void setSelectedPosition(int pos) {
        this.selectedPosition = pos;
        notifyDataSetChanged();
    }

    @Override
    public void bind(ItemChoosingClothesBinding binding, String item, int position) {
        binding.imgChoosingClothes.setRadius(12);
        Glide.with(binding.getRoot().getContext())
                .load(item)
                .placeholder(R.drawable.icon_clothes)
                .error(R.drawable.coat0)
                .into(binding.imgChoosingClothes);

        binding.container.setSelected(position == selectedPosition);

        binding.getRoot().setOnClickListener(v -> {
            listener.onItemClicked(position);
            if (selectedPosition != position) {
                setSelectedPosition(position);
            }
        });
    }

    @Override
    protected ItemChoosingClothesBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return ItemChoosingClothesBinding.inflate(inflater, container, false);
    }
}
