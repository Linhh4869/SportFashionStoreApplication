package com.example.sportfashionstore.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.sportfashionstore.R;
import com.example.sportfashionstore.commonbase.BaseAdapter;
import com.example.sportfashionstore.databinding.ItemHomeProductBinding;
import com.example.sportfashionstore.model.Product;

public class ProductHomeAdapter extends BaseAdapter<Product, ItemHomeProductBinding> {
    @Override
    public void bind(ItemHomeProductBinding binding, Product item, int position) {
        Glide.with(binding.getRoot().getContext())
                .load(item.getImages().get(0))
                .placeholder(R.drawable.shoes2)
                .error(R.drawable.coat0)
                .into(binding.imgProduct);

        binding.tvPrice.setText(item.getDisplayPrice());
        binding.tvRateProduct.setText(item.getRating());
        binding.tvProductDescription.setText(item.getDescription());
        binding.tvSold.setText(item.getSold());
    }

    @Override
    protected ItemHomeProductBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return ItemHomeProductBinding.inflate(inflater, container, false);
    }
}
