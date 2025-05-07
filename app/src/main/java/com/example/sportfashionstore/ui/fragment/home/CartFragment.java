package com.example.sportfashionstore.ui.fragment.home;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.sportfashionstore.R;
import com.example.sportfashionstore.commonbase.BaseFragmentViewModel;
import com.example.sportfashionstore.data.entity.CartEntity;
import com.example.sportfashionstore.databinding.FragmentCartBinding;
import com.example.sportfashionstore.ui.CheckoutActivity;
import com.example.sportfashionstore.ui.adapter.CartAdapter;
import com.example.sportfashionstore.viewmodel.AuthViewModel;
import com.example.sportfashionstore.viewmodel.ChooseProductViewModel;

import java.util.List;
import java.util.Objects;

public class CartFragment extends BaseFragmentViewModel<FragmentCartBinding, ChooseProductViewModel> {
    @Override
    protected FragmentCartBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentCartBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUi() {
        viewModel.getAllCartItems().observe(getViewLifecycleOwner(), data -> {
            if (data == null || data.isEmpty()) {
                binding.layoutError.setVisibility(View.VISIBLE);
                return;
            }

            binding.layoutError.setVisibility(View.GONE);
            CartAdapter cartAdapter = new CartAdapter(new CartAdapter.ItemCartListener() {
                @Override
                public void onDeleteItemCart(CartEntity cart) {
                    viewModel.deleteItemCart(cart);
                }

                @Override
                public void onPayItemCart(CartEntity cartEntity) {
                    Intent intent = new Intent(getActivity(), CheckoutActivity.class);
                    intent.putExtra(CheckoutActivity.KEY_DATA, cartEntity.getId());
                    requireActivity().startActivity(intent);
                }
            });
            cartAdapter.setData(data);
            binding.rcvCart.setAdapter(cartAdapter);
        });
    }
}
