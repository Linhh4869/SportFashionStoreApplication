package com.example.sportfashionstore.ui;

import static android.content.ContentValues.TAG;

import android.graphics.Paint;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.example.sportfashionstore.R;
import com.example.sportfashionstore.commonbase.BaseActivityViewModel;
import com.example.sportfashionstore.commonbase.Resource;
import com.example.sportfashionstore.data.entity.AddressEntity;
import com.example.sportfashionstore.databinding.ActivityCheckoutBinding;
import com.example.sportfashionstore.ui.adapter.InfoPaymentAdapter;
import com.example.sportfashionstore.ui.dialog.UpdateAddressDialog;
import com.example.sportfashionstore.ui.fragment.home.AddressFragment;
import com.example.sportfashionstore.ui.fragment.home.ResultOrderFragment;
import com.example.sportfashionstore.util.Constants;
import com.example.sportfashionstore.util.Helper;
import com.example.sportfashionstore.viewmodel.CheckoutViewModel;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.*;

public class CheckoutActivity extends BaseActivityViewModel<ActivityCheckoutBinding, CheckoutViewModel> {
    public static final String KEY_DATA = "CART_DATA";
    private PaymentSheet paymentSheet;
    private String paymentClientSecret;
    private PaymentSheet.CustomerConfiguration customerConfig;

    @Override
    protected void setupUi() {
        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);
        PaymentConfiguration.init(this, Helper.decodeBase64ToString(Constants.PUBLIC_KEY));
        long id = getIntent().getLongExtra(KEY_DATA, -1);
        viewModel.getInfoPayment(id);
        viewModel.getChooseAddress();

        binding.btnBack.setOnClickListener(v -> {
            viewModel.clearData();
        });

        binding.layoutSelectedAddress.setOnClickListener(v -> {
            showBottomSheet();
        });

        binding.layoutChooseAddress.setOnClickListener(v -> {
            showBottomSheet();
        });

        binding.llPayHome.setOnClickListener(v -> {
            viewModel.setPaymentForm(Constants.CASH_ON_DELIVERY);
        });

        binding.llPayCredit.setOnClickListener(v -> {
            viewModel.setPaymentForm(Constants.PAY_WITH_CREDIT);
        });
    }

    @Override
    protected void setupObservers() {
        viewModel.getCartEntityLiveData().observe(this, cart -> {
            if (cart == null) {
                return;
            }

            binding.setData(cart);
            binding.setInfo(viewModel);

            binding.tvPriceProduct.setText(cart.getDisplayPrice());
            int saleColor = ResourcesCompat.getColor(binding.getRoot().getResources(), R.color.sailing_coral_red, null);
            int notSaleColor = ResourcesCompat.getColor(binding.getRoot().getResources(), R.color.sailing_navy_blue, null);
            if (cart.isSalePrice()) {
                binding.tvPriceProduct.setTextColor(saleColor);
                binding.tvSalePrice.setVisibility(View.VISIBLE);
                binding.tvSalePrice.setText(cart.getDisplaySalePrice());
                binding.tvSalePrice.setPaintFlags(binding.tvPriceProduct.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                binding.tvPriceProduct.setTextColor(notSaleColor);
                binding.tvSalePrice.setVisibility(View.GONE);
            }

            Glide.with(binding.getRoot().getContext())
                    .load(cart.getImage())
                    .placeholder(R.drawable.icon_clothes)
                    .error(R.drawable.coat0)
                    .into(binding.imgProduct);

            binding.tvTotalCost.setText(String.format("%sđ", Helper.formatPrice(viewModel.getTotalPrice())));
            InfoPaymentAdapter infoPaymentAdapter = new InfoPaymentAdapter();
            infoPaymentAdapter.setData(viewModel.getInfoPaymentList());
            binding.rcvPayment.setAdapter(infoPaymentAdapter);

            binding.btnCheckout.setOnClickListener(v -> {
                if (viewModel.getPaymentForm().getValue() == Constants.CASH_ON_DELIVERY) {
                    viewModel.saveOrder(false);
                } else if (viewModel.getPaymentForm().getValue() == Constants.PAY_WITH_CREDIT) {
                    viewModel.prepareShowPaymentSheet();
                }
            });
        });

        viewModel.getSelectedAddress().observe(this, address -> {
            if (address == null) {
                binding.layoutChooseAddress.setVisibility(View.VISIBLE);
                binding.layoutSelectedAddress.setVisibility(View.GONE);
                return;
            }

            binding.layoutChooseAddress.setVisibility(View.GONE);
            binding.layoutSelectedAddress.setVisibility(View.VISIBLE);
            binding.tvName.setText(address.getName());
            binding.tvPhone.setText(address.getPhone());
            binding.tvAddress.setText(address.getAddress());
        });

        viewModel.getEnablePayButton().observe(this, isEnable -> {
            binding.btnCheckout.setEnabled(isEnable);
        });

        viewModel.getSaveOrderLiveData().observe(this, resource -> {
            if (resource.state.equals(Resource.State.SUCCESS) && resource.data != null) {
                binding.container.setVisibility(View.VISIBLE);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new ResultOrderFragment())
                        .commit();
            }
        });

        viewModel.getCusConfigLiveData().observe(this, resource -> {
            if (resource.state.equals(Resource.State.SUCCESS) && resource.data != null) {
                Helper.showMyToast(this, "In process!");
                customerConfig = resource.data;
                paymentClientSecret = viewModel.getPaymentClientSecretLiveData();
                final PaymentSheet.Configuration configuration = new PaymentSheet.Configuration.Builder("Example, Inc.")
                        .customer(customerConfig)
                        // Set `allowsDelayedPaymentMethods` to true if your business handles payment methods
                        // delayed notification payment methods like US bank accounts.
                        .allowsDelayedPaymentMethods(true)
                        .build();
                paymentSheet.presentWithPaymentIntent(
                        paymentClientSecret,
                        configuration
                );
            }
        });

        viewModel.getPaymentForm().observe(this, payForm -> {
            boolean isCash = payForm == Constants.CASH_ON_DELIVERY;
            binding.icPayHome.setImageResource(isCash ? R.drawable.ic_blue_check : R.drawable.ic_blue_outline_circle);
            binding.icPayCredit.setImageResource(isCash ? R.drawable.ic_blue_outline_circle : R.drawable.ic_blue_check);
        });
    }

    @Override
    protected ActivityCheckoutBinding createViewBinding() {
        return ActivityCheckoutBinding.inflate(getLayoutInflater());
    }

    private void showBottomSheet() {
        AddressFragment addressFragment = new AddressFragment(
                (address, tag) -> new Handler().postDelayed(
                        () -> showDialog(address, tag), 500)
        );
        addressFragment.show(getSupportFragmentManager(), "");
    }

    private void showDialog(AddressEntity address, int tag) {
        UpdateAddressDialog updateAddressDialog = new UpdateAddressDialog(
                this, this, address, tag,
                () -> new Handler().postDelayed(this::showBottomSheet, 500));
        updateAddressDialog.show();
    }

    private void onPaymentSheetResult(final PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Log.d(TAG, "Canceled");
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            Log.e(TAG, "Got error: ", ((PaymentSheetResult.Failed) paymentSheetResult).getError());
            Helper.showMyToast(this, "Xảy ra lỗi trong quá trình thanh toán, vui lòng thử lại!");
        } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            // Display for example, an order confirmation screen
            Log.d(TAG, "Completed");
            viewModel.saveOrder(true);
        }
    }
}
