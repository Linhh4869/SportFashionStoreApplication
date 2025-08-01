package com.example.sportfashionstore.ui;

import static com.example.sportfashionstore.util.Constants.CREATE;
import static com.example.sportfashionstore.util.Constants.DELETE;
import static com.example.sportfashionstore.util.Constants.UPDATE;

import android.content.Intent;

import com.example.sportfashionstore.ui.commonbase.BaseActivityViewModel;
import com.example.sportfashionstore.ui.commonbase.Resource;
import com.example.sportfashionstore.databinding.ActivityCrudProductBinding;
import com.example.sportfashionstore.model.Product;
import com.example.sportfashionstore.model.ProductVariant;
import com.example.sportfashionstore.ui.adapter.VariantAdapter;
import com.example.sportfashionstore.ui.dialog.CommonConfirmDialog;
import com.example.sportfashionstore.ui.fragment.owner.VariantManagementFragment;
import com.example.sportfashionstore.util.Constants;
import com.example.sportfashionstore.util.Helper;
import com.example.sportfashionstore.viewmodel.ProductManagementViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CRUDProductActivity extends BaseActivityViewModel<ActivityCrudProductBinding, ProductManagementViewModel> {
    public static final String KEY_CURD = "curd";
    public static final String KEY_PRODUCT = "product";
    private String typeCurd = Constants.ADD_PRODUCT;
    private VariantAdapter variantAdapter;
    private boolean addNewProduct;

    @Override
    protected void setupUi() {
        binding.setViewModel(viewModel);
        if (getIntent().getStringExtra(KEY_CURD) != null) {
            typeCurd = getIntent().getStringExtra(KEY_CURD);
        }

        if (typeCurd.equals(Constants.ADD_PRODUCT)) {
            addNewProduct = true;
            binding.tvScreen.setText("Thêm sản phẩm mới");
            binding.btnCurdProduct.setText("Thêm sản phẩm");
        } else {
            addNewProduct = false;
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
            public void onDelete(ProductVariant variant, int position) {
                showDeleteVariantDialog(variant, position);
            }

            @Override
            public void onEdit(ProductVariant variant, int position) {
                showManagementVariantBottomSheet(Constants.EDIT_VARIANT, variant, position);
            }
        });
        binding.rcvVariant.setAdapter(variantAdapter);
        binding.btn25.setOnClickListener(v -> onChangeDiscount(25));

        binding.btn50.setOnClickListener(v -> onChangeDiscount(50));

        binding.btn75.setOnClickListener(v -> onChangeDiscount(75));

        binding.btnZeroSale.setOnClickListener(v -> onChangeDiscount(0));

        binding.btnBack.setOnClickListener(v -> onBackPressed());

        binding.btnAddVariant.setOnClickListener(v -> showManagementVariantBottomSheet(Constants.ADD_VARIANT, null, CREATE));

        binding.inputPrice.setOnTextChangedListener(text -> viewModel.setPriceDisplay(text));

        binding.inputSalePrice.setOnTextChangedListener(sale -> viewModel.setSalePriceDisplay(sale));

        binding.btnCurdProduct.setOnClickListener(v -> {
            viewModel.setCategoryProduct(binding.spinnerCategory.getActualSelectItemPosition());
            onSubmitProduct();
        });
    }

    @Override
    protected void setupObservers() {
        viewModel.getProductLiveData().observe(this, resource -> {
            if (resource.state.equals(Resource.State.SUCCESS) && resource.data != null) {
                Product product = resource.data;
                binding.setProduct(product);
                int cateSelected = viewModel.getCateGorySelected(product.getCategoryId());
                binding.spinnerCategory.setItemSelected(cateSelected - 1);
            }
        });

        viewModel.getDynamicVariants().observe(this, resource -> {
            if (resource.state.equals(Resource.State.SUCCESS) && resource.data != null) {
                variantAdapter.setData(resource.data);
            }
        });

        viewModel.getOnCURDVariant().observe(this, this::showToast);

        viewModel.getOnSubmitProduct().observe(this, success -> {
            setResult(RESULT_OK, new Intent());
            finish();
        });

        viewModel.getErrorSubmitProduct().observe(this, this::showToast);
    }

    @Override
    protected ActivityCrudProductBinding createViewBinding() {
        return ActivityCrudProductBinding.inflate(getLayoutInflater());
    }

    private void showManagementVariantBottomSheet(String tag, ProductVariant variant, int position) {
        VariantManagementFragment variantManagementFragment = new VariantManagementFragment(submitVariant -> {
            int typeVariant = tag.equals(Constants.ADD_VARIANT) ? CREATE : UPDATE;
            curdVariant(typeVariant, submitVariant, position);
        });
        variantManagementFragment.setVariant(variant);
        variantManagementFragment.show(getSupportFragmentManager(), tag);
    }

    private void onChangeDiscount(int discountPercent) {
        viewModel.onDiscountSelected(discountPercent);
        binding.inputSalePrice.setText(viewModel.getSalePriceDisplay());
    }

    private void showDeleteVariantDialog(ProductVariant variant, int position) {
        new CommonConfirmDialog(this)
                .setContent("Bạn có chắc chắn muốn xóa mẫu hàng hóa này?")
                .setCancelable(true)
                .setOnConfirmListener(() -> curdVariant(DELETE, variant, position))
                .show();
    }

    private void curdVariant(int typeVariant, ProductVariant submitVariant, int position) {
        if (addNewProduct) {
            String variantJson = Helper.objectToJson(submitVariant);
            viewModel.curdVariantTypeAddNew(typeVariant, position, variantJson);
        } else {
            viewModel.curdVariantTypeUpdate(typeVariant, submitVariant);
        }
    }

    private void onSubmitProduct() {
        if (addNewProduct) {
            viewModel.onCreateNewProduct();
        } else {
            viewModel.onUpdateProduct();
        }
    }
}
