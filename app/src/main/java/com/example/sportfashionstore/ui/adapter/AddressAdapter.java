package com.example.sportfashionstore.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sportfashionstore.callback.OnItemClickListener;
import com.example.sportfashionstore.commonbase.BaseAdapter;
import com.example.sportfashionstore.data.entity.AddressEntity;
import com.example.sportfashionstore.databinding.ItemAddressBinding;

import java.util.concurrent.atomic.AtomicBoolean;

public class AddressAdapter extends BaseAdapter<AddressEntity, ItemAddressBinding> {
    private final OnItemClickListener<AddressEntity> listener;
    private OnItemClickListener<AddressEntity> editAddressListener;

    public AddressAdapter(OnItemClickListener<AddressEntity> listener) {
        this.listener = listener;
    }

    public void setEditAddressListener(OnItemClickListener<AddressEntity> mListener) {
        this.editAddressListener = mListener;
    }

    @Override
    public void bind(ItemAddressBinding binding, AddressEntity item, int position) {
        AtomicBoolean isCheck = new AtomicBoolean(item.isSelected() == 1);
        binding.btnCheck.setChecked(isCheck.get());
        binding.tvDefault.setVisibility(item.isDefaultAddress() == 1 ? View.VISIBLE : View.GONE);
        binding.tvName.setText(item.getName());
        binding.tvPhone.setText(item.getPhone());
        binding.tvAddress.setText(item.getAddress());

        binding.btnEditAddress.setOnClickListener(v -> {
            if (editAddressListener != null) {
                editAddressListener.onItemClicked(item);
            }
        });

        binding.btnCheck.setClickable(false);

        binding.getRoot().setOnClickListener(v -> {
            binding.btnCheck.setChecked(true);
            item.setSelected(1);
            listener.onItemClicked(item);
        });
    }

    @Override
    protected ItemAddressBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return ItemAddressBinding.inflate(inflater, container, false);
    }
}
