package com.example.sportfashionstore.ui;

import android.os.Handler;

import com.example.sportfashionstore.commonbase.BaseActivityViewModel;
import com.example.sportfashionstore.commonbase.Resource;
import com.example.sportfashionstore.databinding.ActivityCrudProductBinding;
import com.example.sportfashionstore.model.Category;
import com.example.sportfashionstore.model.Product;
import com.example.sportfashionstore.model.ProductVariant;
import com.example.sportfashionstore.ui.adapter.VariantAdapter;
import com.example.sportfashionstore.ui.dialog.CommonConfirmDialog;
import com.example.sportfashionstore.ui.fragment.owner.VariantManagementFragment;
import com.example.sportfashionstore.ui.widget.CommonTextInput;
import com.example.sportfashionstore.util.Constants;
import com.example.sportfashionstore.viewmodel.ProductManagementViewModel;

import java.util.ArrayList;
import java.util.List;

public class CRUDProductActivity extends BaseActivityViewModel<ActivityCrudProductBinding, ProductManagementViewModel> {
    public static final String KEY_CURD = "curd";
    public static final String KEY_PRODUCT = "product";
    public static final String KEY_CATEGORIES = "categories";
    private ArrayList<Category> categories = new ArrayList<>();
    private String typeCurd = Constants.ADD_PRODUCT;
    private VariantAdapter variantAdapter;
    private Product product;

    @Override
    protected void setupUi() {
        binding.setViewModel(viewModel);
        if (getIntent().getStringExtra(KEY_CURD) != null) {
            typeCurd = getIntent().getStringExtra(KEY_CURD);
        }

        if (typeCurd.equals(Constants.ADD_PRODUCT)) {
            binding.tvScreen.setText("Thêm sản phẩm mới");
            binding.btnCurdProduct.setText("Thêm sản phẩm");
        } else {
            binding.tvScreen.setText("Cập nhật sản phẩm");
            binding.btnCurdProduct.setText("Cập nhật");
            String productId = getIntent().getStringExtra(KEY_PRODUCT);
            if (productId != null && !productId.isEmpty()) {
                viewModel.getProductToCURD(productId);
            }
        }

        binding.spinnerCategory.setData(viewModel.getCategoryString(), false);

        variantAdapter = new VariantAdapter(new VariantAdapter.OnUDVariantListener() {
            @Override
            public void onDelete(int position) {
                showDeleteVariantDialog();
            }

            @Override
            public void onEdit(ProductVariant variant) {
                showManagementVariantBottomSheet(Constants.EDIT_VARIANT, variant);
            }
        });
        binding.rcvVariant.setAdapter(variantAdapter);
        binding.btn25.setOnClickListener(v -> {
            onChangeDiscount(25);
        });

        binding.btn50.setOnClickListener(v -> {
            onChangeDiscount(50);
        });

        binding.btn75.setOnClickListener(v -> {
            onChangeDiscount(75);
        });

        binding.btnZeroSale.setOnClickListener(v -> {
            onChangeDiscount(0);
        });

        binding.btnBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.btnAddVariant.setOnClickListener(v -> {
            showManagementVariantBottomSheet(Constants.ADD_VARIANT, null);
        });

        binding.inputPrice.setOnTextChangedListener(text -> {
            viewModel.setSalePriceDisplay(text);
        });
    }

    @Override
    protected void setupObservers() {
        viewModel.getProductLiveData().observe(this, resource -> {
            if (resource.state.equals(Resource.State.SUCCESS) && resource.data != null) {
                Product product = resource.data;
                binding.setProduct(product);
                variantAdapter.setData(product.getProductVariants());
            }
        });

        viewModel.getDynamicVariants().observe(this, resource -> {
            if (resource.state.equals(Resource.State.SUCCESS) && resource.data != null) {
                variantAdapter.setData(resource.data);
            }
        });

        viewModel.getOnCURDVariant().observe(this, this::showToast);
    }

    @Override
    protected ActivityCrudProductBinding createViewBinding() {
        return ActivityCrudProductBinding.inflate(getLayoutInflater());
    }

    private void showManagementVariantBottomSheet(String tag, ProductVariant variant) {
        VariantManagementFragment variantManagementFragment = new VariantManagementFragment(submitVariant -> {
            loadingDialog.show();
            viewModel.updateVariantList(tag.equals(Constants.ADD_VARIANT), submitVariant);
            new Handler().postDelayed(() -> {
                loadingDialog.dismiss();
                variantAdapter.setData(viewModel.getSubmitVariants());
            }, 1000);
        });
        variantManagementFragment.setVariant(variant);
        variantManagementFragment.show(getSupportFragmentManager(), tag);
    }

    private void onChangeDiscount(int discountPercent) {
        viewModel.onDiscountSelected(discountPercent);
        binding.inputSalePrice.setText(viewModel.getSalePriceDisplay());
    }

    private void showDeleteVariantDialog() {
        try {
            new CommonConfirmDialog(this)
                    .setContent("Bạn có chắc chắn muốn xóa mẫu hàng hóa này?")
                    .setCancelable(true)
                    .setOnConfirmListener(() -> {

                    })
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
