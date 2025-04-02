package com.example.sportfashionstore.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sportfashionstore.callback.OnItemClickListener;
import com.example.sportfashionstore.commonbase.BaseAdapter;
import com.example.sportfashionstore.databinding.ItemUtilityPersonalBinding;
import com.example.sportfashionstore.model.FeatureModel;

public class UtilityAdapter extends BaseAdapter<FeatureModel, ItemUtilityPersonalBinding> {
    private final OnItemClickListener<FeatureModel> callBack;

    public UtilityAdapter(OnItemClickListener<FeatureModel> callBack) {
        this.callBack = callBack;
    }
    @Override
    public void bind(ItemUtilityPersonalBinding binding, FeatureModel item, int position) {
        binding.setUtility(item);
        binding.icUtility.setImageResource(item.getIcon());
        binding.line.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
        binding.getRoot().setOnClickListener(v -> {
            callBack.onItemClicked(item);
        });
    }

    @Override
    protected ItemUtilityPersonalBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return ItemUtilityPersonalBinding.inflate(inflater, container, false);
    }
}
