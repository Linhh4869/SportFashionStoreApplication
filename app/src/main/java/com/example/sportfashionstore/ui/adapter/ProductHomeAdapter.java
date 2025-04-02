package com.example.sportfashionstore.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.example.sportfashionstore.R;
import com.example.sportfashionstore.commonbase.BaseAdapter;
import com.example.sportfashionstore.databinding.ItemHomeProductBinding;
import com.example.sportfashionstore.model.Product;
import com.example.sportfashionstore.util.Helper;

public class ProductHomeAdapter extends BaseAdapter<Product, ItemHomeProductBinding> {
    @Override
    public void bind(ItemHomeProductBinding binding, Product item, int position) {
        binding.setProduct(item);
        Glide.with(binding.getRoot().getContext())
                .load(item.getImages().get(0))
                .placeholder(R.drawable.shoes2)
                .error(R.drawable.coat0)
                .into(binding.imgProduct);

        int saleColor = ResourcesCompat.getColor(binding.getRoot().getResources(), R.color.sailing_coral_red, null);
        int notSaleColor = ResourcesCompat.getColor(binding.getRoot().getResources(), R.color.sailing_navy_blue, null);
        binding.tvPrice.setTextColor(item.isSaleClothes() ? saleColor : notSaleColor);

        binding.getRoot().setOnClickListener(v -> {
            Helper.showMyToast(binding.getRoot().getContext(), item.getName());
        });
    }

    @Override
    protected ItemHomeProductBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return ItemHomeProductBinding.inflate(inflater, container, false);
    }
}
