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
    private long addressId = -1;
    private AddressFragment.OnDismissDialog mListener;

    public UpdateAddressDialog(@NonNull Context context, LifecycleOwner lifecycleOwner, long addressId, int typeDialog, AddressFragment.OnDismissDialog mListener) {
        super(context);
        this.lifecycleOwner = lifecycleOwner;
        this.addressId = addressId;
        this.typeDialog = typeDialog;
        this.mListener = mListener;
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
        } else if (typeDialog == UPDATE_ADDRESS) {
            binding.btnDeleteAddress.setVisibility(View.VISIBLE);
            binding.tvTitle.setText("Sửa địa chỉ");
        }

        binding.btnClose.setOnClickListener(v -> {
            dismiss();
        });

        binding.btnSetDefault.setOnClickListener(v -> {
            viewModel.setDefaultAddressLiveData(binding.btnSetDefault.isChecked());
        });

        binding.btnDeleteAddress.setOnClickListener(v -> {
            viewModel.deleteAddress(addressId);
            dismiss();
        });

        binding.btnConfirm.setOnClickListener(v -> {
            if (typeDialog == ADD_ADDRESS) {
                viewModel.addNewAddress();
            } else if (typeDialog == UPDATE_ADDRESS) {
                viewModel.updateAddress(addressId);
            }
            dismiss();
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
}
