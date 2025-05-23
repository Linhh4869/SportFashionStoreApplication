package com.example.sportfashionstore.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.sportfashionstore.R;
import com.example.sportfashionstore.commonbase.BaseAdapter;
import com.example.sportfashionstore.databinding.ItemVariantBinding;
import com.example.sportfashionstore.model.ProductVariant;

public class VariantAdapter extends BaseAdapter<ProductVariant, ItemVariantBinding> {
    private final OnUDVariantListener listener;

    public VariantAdapter(OnUDVariantListener listener) {
        this.listener = listener;
    }
    @Override
    public void bind(ItemVariantBinding binding, ProductVariant item, int position) {
        binding.tvLabel.setText(item.getDesc());

        Glide.with(binding.getRoot().getContext())
                .load(item.getImage())
                .placeholder(R.drawable.icon_clothes)
                .error(R.drawable.coat0)
                .into(binding.imgProduct);

        binding.btnDeleteVariant.setOnClickListener(v -> {
            listener.onDelete(position);
        });

        binding.btnEditVariant.setOnClickListener(v -> {
            listener.onEdit(item);
        });
    }

    @Override
    protected ItemVariantBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return ItemVariantBinding.inflate(inflater, container, false);
    }

    public interface OnUDVariantListener {
        void onDelete(int position);

        void onEdit(ProductVariant variant);
    }
}
