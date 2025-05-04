package com.example.sportfashionstore.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.res.ResourcesCompat;

import com.example.sportfashionstore.R;
import com.example.sportfashionstore.callback.OnItemClickListener;
import com.example.sportfashionstore.commonbase.BaseAdapter;
import com.example.sportfashionstore.databinding.ItemChooseClothesBinding;
import com.example.sportfashionstore.model.SizeModel;

public class SizeAdapter extends BaseAdapter<SizeModel, ItemChooseClothesBinding> {
    private int selectedPosition = -1;
    private final OnItemClickListener<SizeModel> listener;

    public SizeAdapter(OnItemClickListener<SizeModel> listener) {
        this.listener = listener;
    }

    @Override
    public void bind(ItemChooseClothesBinding binding, SizeModel item, int position) {
        binding.tvLabel.setText(item.getSize());
        binding.imgProduct.setVisibility(View.GONE);

        binding.container.setAlpha((float) (item.isAvailable() ? 1 : 0.5));
        binding.container.setEnabled(item.isAvailable());

        boolean isSelected = position == selectedPosition && item.isAvailable();
        binding.container.setSelected(isSelected);
        int color = ResourcesCompat.getColor(binding.getRoot().getContext().getResources(), R.color.model_black, null);
        int selectedColor = ResourcesCompat.getColor(binding.getRoot().getContext().getResources(), R.color.ocean_blue, null);
        binding.tvLabel.setTextColor(isSelected ? selectedColor : color);

        binding.getRoot().setOnClickListener(v -> {
            if (selectedPosition != position) {
                selectedPosition = position;
                notifyDataSetChanged();
            }

            listener.onItemClicked(item);
        });
    }

    public void resetState() {
        selectedPosition = -1;
        notifyDataSetChanged();
    }

    @Override
    protected ItemChooseClothesBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return ItemChooseClothesBinding.inflate(inflater, container, false);
    }
}
