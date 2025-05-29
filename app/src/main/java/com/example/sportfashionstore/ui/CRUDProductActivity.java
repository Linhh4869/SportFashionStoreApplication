package com.example.sportfashionstore.ui;

import com.example.sportfashionstore.commonbase.BaseActivityViewModel;
import com.example.sportfashionstore.commonbase.Resource;
import com.example.sportfashionstore.custom.CustomSpinner;
import com.example.sportfashionstore.databinding.ActivityCrudProductBinding;
import com.example.sportfashionstore.model.Category;
import com.example.sportfashionstore.model.Product;
import com.example.sportfashionstore.model.ProductVariant;
import com.example.sportfashionstore.ui.adapter.VariantAdapter;
import com.example.sportfashionstore.util.Constants;
import com.example.sportfashionstore.viewmodel.ProductManagementViewModel;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CRUDProductActivity extends BaseActivityViewModel<ActivityCrudProductBinding, ProductManagementViewModel> {
    public static final String KEY_CURD = "curd";
    public static final String KEY_PRODUCT = "product";
    public static final String KEY_CATEGORIES = "categories";
    private ArrayList<Category> categories = new ArrayList<>();
    private String typeCurd = Constants.ADD_PRODUCT;
    private VariantAdapter variantAdapter;

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
        }
        viewModel.getAllCategoryList(false);
//        String categoriesJson = getIntent().getStringExtra(KEY_CATEGORIES);
//        if (categoriesJson != null && !categoriesJson.isEmpty()) {
//            try {
//                Gson gson = new Gson();
//                Type listType = new TypeToken<ArrayList<Category>>(){}.getType();
//                categories = gson.fromJson(categoriesJson, listType);
//            } catch (Exception e) {
//                categories = new ArrayList<>();
//            }
//        } else {
//            categories = new ArrayList<>();
//        }
//        binding.spinnerCategory.setData(viewModel.getCategoryString(categories), false);

        variantAdapter = new VariantAdapter(new VariantAdapter.OnUDVariantListener() {
            @Override
            public void onDelete(int position) {

            }

            @Override
            public void onEdit(ProductVariant variant) {

            }
        });
        binding.rcvVariant.setAdapter(variantAdapter);
        binding.btn25.setOnClickListener(v -> {
            if (viewModel.getPrice() == null || viewModel.getPrice().getValue() == null) return;
            int price = viewModel.getPrice().getValue();
            viewModel.setSalePrice((int) (price * 0.25));
        });

        binding.btn50.setOnClickListener(v -> {
            if (viewModel.getPrice() == null || viewModel.getPrice().getValue() == null) return;
            int price = viewModel.getPrice().getValue();
            viewModel.setSalePrice((int) (price * 0.5));
        });

        binding.btn75.setOnClickListener(v -> {
            if (viewModel.getPrice() == null || viewModel.getPrice().getValue() == null) return;
            int price = viewModel.getPrice().getValue();
            viewModel.setSalePrice((int) (price * 0.75));
        });
//        String productId = getIntent().getStringExtra(KEY_PRODUCT);
//        if (productId == null)
//            return;
//
//        viewModel.getProductToCURD(productId);
        binding.btnZeroSale.setOnClickListener(v -> {
            viewModel.setSalePrice(0);
        });
    }

    @Override
    protected void setupObservers() {
//        viewModel.getProductLiveData().observe(this, resource -> {
//            if (resource.state.equals(Resource.State.SUCCESS) && resource.data != null) {
//                Product product = resource.data;
//                binding.setProduct(product);
//                viewModel.setVariantLiveData(product.getProductVariants());
//                binding.edtPrice.setText(String.valueOf(product.getPrice()));
//                binding.edtSalePrice.setText(String.valueOf(product.getSalePrice()));
//            }
//        });
//
//        viewModel.getVariantLiveData().observe(this, variants -> {
//            variantAdapter.setData(variants);
//        });
//
//        viewModel.getSalePrice().observe(this, salePrice -> {
//            binding.edtSalePrice.setText(String.valueOf(salePrice));
//        });

        viewModel.getCategoryList().observe(this, list -> {
            binding.spinnerCategory.setData(viewModel.getCategoryString(list), false);
        });
    }
}
