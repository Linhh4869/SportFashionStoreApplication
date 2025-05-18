package com.example.sportfashionstore.ui.fragment.home;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;

import com.example.sportfashionstore.callback.OnItemClickListener;
import com.example.sportfashionstore.commonbase.BaseBottomSheetFragment;
import com.example.sportfashionstore.data.entity.AddressEntity;
import com.example.sportfashionstore.databinding.FragmentChooseAddressBinding;
import com.example.sportfashionstore.ui.adapter.AddressAdapter;
import com.example.sportfashionstore.ui.dialog.UpdateAddressDialog;
import com.example.sportfashionstore.viewmodel.CheckoutViewModel;

import java.util.ArrayList;
import java.util.List;

public class AddressFragment extends BaseBottomSheetFragment<FragmentChooseAddressBinding, CheckoutViewModel> {
    private AddressAdapter addressAdapter;
    private final OnDismissDialog mListener;

    public AddressFragment(OnDismissDialog mListener) {
        this.mListener = mListener;
    }

    @Override
    protected FragmentChooseAddressBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentChooseAddressBinding.inflate(inflater, container, false);
    }

    @Override
    protected CheckoutViewModel getViewModel() {
        return new ViewModelProvider(requireActivity()).get(CheckoutViewModel.class);
    }

    @Override
    protected void initView() {
        viewModel.getAllAddressList();
        addressAdapter = new AddressAdapter(item -> {
            viewModel.updateSelectedAddress(item);
            dismiss();
        });

        addressAdapter.setEditAddressListener(item -> {
            viewModel.setDataUpdateDialog(item);
            showDialog(item, UpdateAddressDialog.UPDATE_ADDRESS);
        });

        binding.rcvAddress.setAdapter(addressAdapter);
        binding.btnAddAddress.setOnClickListener(v -> {
            viewModel.setDataUpdateDialog(null);
            showDialog(null, UpdateAddressDialog.ADD_ADDRESS);
        });
    }

    @Override
    protected void observerData() {
        viewModel.getAllAddress().observe(getViewLifecycleOwner(), list -> {
            if (list == null || list.isEmpty()) {
                return;
            }
            addressAdapter.setData(list);
        });

        viewModel.getRefreshEvent().observe(getViewLifecycleOwner(), unit -> {
            viewModel.getAllAddressList();
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mListener.onDismiss();
    }

    private void showDialog(AddressEntity address, int tag) {
        UpdateAddressDialog updateAddressDialog = new UpdateAddressDialog(getActivity(), getViewLifecycleOwner(), address, tag, () -> {
            viewModel.triggerRefresh();
        });
        updateAddressDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.getAllAddressList();
    }

    public interface OnDismissDialog {
        void onDismiss();
    }
}
