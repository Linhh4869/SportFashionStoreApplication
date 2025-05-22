package com.example.sportfashionstore.ui.dialog;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.sportfashionstore.commonbase.BaseDialog;
import com.example.sportfashionstore.data.entity.AddressEntity;
import com.example.sportfashionstore.databinding.DialogUpdateAddressBinding;
import com.example.sportfashionstore.ui.fragment.home.AddressFragment;
import com.example.sportfashionstore.viewmodel.CheckoutViewModel;

public class UpdateAddressDialog extends BaseDialog<DialogUpdateAddressBinding, CheckoutViewModel> {
    public static final int UPDATE_ADDRESS = 1;
    public static final int ADD_ADDRESS = 2;
    private final LifecycleOwner lifecycleOwner;
    private int typeDialog = -1;
    private final AddressEntity address;
    private final OnDismissListener mListener;

    public UpdateAddressDialog(@NonNull Context context, LifecycleOwner lifecycleOwner, AddressEntity address, int typeDialog, OnDismissListener listener) {
        super(context);
        this.lifecycleOwner = lifecycleOwner;
        this.address = address;
        this.typeDialog = typeDialog;
        this.mListener = listener;
    }

    @Override
    protected DialogUpdateAddressBinding getViewBinding(LayoutInflater inflater) {
        return DialogUpdateAddressBinding.inflate(inflater, null, false);
    }

    @Override
    protected CheckoutViewModel getViewModel() {
        return new ViewModelProvider((ViewModelStoreOwner) lifecycleOwner).get(CheckoutViewModel.class);
    }

    @Override
    protected void initView() {
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(lifecycleOwner);
        if (typeDialog == ADD_ADDRESS) {
            binding.btnDeleteAddress.setVisibility(View.GONE);
            binding.tvTitle.setText("Địa chỉ mới");
            binding.edtAddress.setText("");
            binding.edtName.setText("");
            binding.edtPhone.setText("");
            binding.btnSetDefault.setChecked(false);
        } else if (typeDialog == UPDATE_ADDRESS) {
            binding.btnDeleteAddress.setVisibility(View.VISIBLE);
            binding.tvTitle.setText("Sửa địa chỉ");
            binding.edtAddress.setText(address.getAddress());
            binding.edtName.setText(address.getName());
            binding.edtPhone.setText(address.getPhone());
            binding.btnSetDefault.setChecked(address.isDefaultAddress() == 1);
        }

        binding.btnClose.setOnClickListener(v -> {
            dismiss();
        });

        binding.btnSetDefault.setOnClickListener(v -> {
            viewModel.setDefaultAddressLiveData(binding.btnSetDefault.isChecked());
        });

        binding.btnDeleteAddress.setOnClickListener(v -> {
            viewModel.deleteAddress(address);
            dismiss();
            mListener.onDismiss();
        });

        binding.btnConfirm.setOnClickListener(v -> {
            if (typeDialog == ADD_ADDRESS) {
                viewModel.addNewAddress();
            } else if (typeDialog == UPDATE_ADDRESS) {
                viewModel.updateAddress(address);
            }
            dismiss();
            mListener.onDismiss();
        });
    }

    @Override
    protected void setupListener() {
        viewModel.getIsButtonDialogEnabled().observe(lifecycleOwner, isEnable -> {
            binding.btnConfirm.setEnabled(isEnable);
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mListener.onDismiss();
    }

    public static interface OnDismissListener {
        void  onDismiss();
    }
}
