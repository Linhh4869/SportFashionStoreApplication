package com.example.sportfashionstore.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sportfashionstore.commonbase.BaseAdapter;
import com.example.sportfashionstore.databinding.ItemUtilityPersonalBinding;
import com.example.sportfashionstore.model.FeatureModel;

public class UtilityAdapter extends BaseAdapter<FeatureModel, ItemUtilityPersonalBinding> {
    @Override
    public void bind(ItemUtilityPersonalBinding binding, FeatureModel item, int position) {
        binding.setUtility(item);
        binding.icUtility.setImageResource(item.getIcon());
        binding.line.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
        binding.getRoot().setOnClickListener(v -> {
            Toast.makeText(binding.getRoot().getContext(), item.getLabel(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected ItemUtilityPersonalBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return ItemUtilityPersonalBinding.inflate(inflater, container, false);
    }
}
