package com.example.sportfashionstore.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.example.sportfashionstore.R;
import com.example.sportfashionstore.app.MyApplication;
import com.example.sportfashionstore.callback.OnItemClickListener;
import com.example.sportfashionstore.commonbase.BaseAdapter;
import com.example.sportfashionstore.databinding.ItemHomeProductBinding;
import com.example.sportfashionstore.model.Product;
import com.example.sportfashionstore.util.Constants;

public class ProductHomeAdapter extends BaseAdapter<Product, ItemHomeProductBinding> {
    private final OnItemClickListener<Product> listener;

    public ProductHomeAdapter(OnItemClickListener<Product> listener) {
        this.listener = listener;
    }

    @Override
    public void bind(ItemHomeProductBinding binding, Product item, int position) {
        binding.setProduct(item);
        Glide.with(binding.getRoot().getContext())
                .load(item.getImages().get(0))
                .placeholder(R.drawable.icon_clothes)
                .error(R.drawable.coat0)
                .into(binding.imgProduct);

        String currentRole = MyApplication.getSharePrefHelper().getRole();
        int saleColor = ResourcesCompat.getColor(binding.getRoot().getResources(), R.color.sailing_coral_red, null);
        int notSaleColor = ResourcesCompat.getColor(binding.getRoot().getResources(), R.color.sailing_navy_blue, null);
        binding.tvPrice.setTextColor(item.isSaleClothes() ? saleColor : notSaleColor);
        binding.llEdit.setVisibility(currentRole.equals(Constants.Role.OWNER) ? View.VISIBLE : View.GONE);

        binding.getRoot().setOnClickListener(v -> {
            listener.onItemClicked(item);
        });
    }

    @Override
    protected ItemHomeProductBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return ItemHomeProductBinding.inflate(inflater, container, false);
    }
}
