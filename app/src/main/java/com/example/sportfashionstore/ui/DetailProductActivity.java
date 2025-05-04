package com.example.sportfashionstore.ui;

import android.graphics.Paint;
import android.view.Gravity;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sportfashionstore.R;
import com.example.sportfashionstore.commonbase.BaseActivityViewModel;
import com.example.sportfashionstore.commonbase.Resource;
import com.example.sportfashionstore.databinding.ActivityProductBinding;
import com.example.sportfashionstore.model.Product;
import com.example.sportfashionstore.model.ProductVariant;
import com.example.sportfashionstore.ui.adapter.ChoosingImageAdapter;
import com.example.sportfashionstore.ui.adapter.CommonImageAdapter;
import com.example.sportfashionstore.ui.fragment.home.ChooseProductFragment;
import com.example.sportfashionstore.util.Constants;
import com.example.sportfashionstore.viewmodel.ChooseProductViewModel;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DetailProductActivity extends BaseActivityViewModel<ActivityProductBinding, ChooseProductViewModel> {
    public static final String KEY_PRODUCT_ITEM = "key_product_item";
    private CommonImageAdapter commonImageAdapter;
    private ChoosingImageAdapter choosingImageAdapter;
    int productSize = 0;
    private final List<ProductVariant> productVariantList = new ArrayList<>();
    private Product product;

    @Override
    protected void setupUi() {
        Product product = (Product) getIntent().getSerializableExtra(KEY_PRODUCT_ITEM);
        if (product == null) product = new Product();

        viewModel.getProductById(product.getId());

        commonImageAdapter = new CommonImageAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.rcvImage.setAdapter(commonImageAdapter);
        binding.rcvImage.setLayoutManager(layoutManager);
        GravitySnapHelper snapHelper = new GravitySnapHelper(Gravity.CENTER);
        snapHelper.attachToRecyclerView(binding.rcvImage);

        choosingImageAdapter = new ChoosingImageAdapter(position -> {
            if (position < productSize) {
                layoutManager.scrollToPosition(position);
                updatePositionText(position);
            }
        });
        binding.rcvItem.setAdapter(choosingImageAdapter);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.rcvItem.setLayoutManager(layoutManager1);

        snapHelper.setSnapListener(position -> {
            updatePositionText(position);
            choosingImageAdapter.setSelectedPosition(position);
        });

        binding.btnBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.btnCart.setOnClickListener(v -> {
            showBottomSheet(Constants.ADD_CART);
        });

        binding.btnPay.setOnClickListener(v -> {
            showBottomSheet(Constants.PAY_NOW);
        });
    }

    @Override
    protected void setupObservers() {
        viewModel.getVariantsLiveData().observe(this, resource -> {
            if (resource.state.equals(Resource.State.SUCCESS) && resource.data != null) {
                productVariantList.clear();
                productVariantList.addAll(resource.data);
                product = Objects.requireNonNull(viewModel.getProductLiveData().getValue()).data;
                commonImageAdapter.setData(product.getImages());
                choosingImageAdapter.setData(product.getImages());

                binding.setProduct(product);
                productSize = product.getImages().size();
                updatePositionText(0);

                binding.tvPrice.setText(product.getDisplayPrice());
                int saleColor = ResourcesCompat.getColor(binding.getRoot().getResources(), R.color.sailing_coral_red, null);
                int notSaleColor = ResourcesCompat.getColor(binding.getRoot().getResources(), R.color.sailing_navy_blue, null);
                if (product.isSaleClothes()) {
                    binding.tvPrice.setTextColor(saleColor);
                    binding.tvSalePrice.setVisibility(View.VISIBLE);
                    binding.tvSalePrice.setText(product.getDisplaySalePrice());
                    binding.tvSalePrice.setPaintFlags(binding.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    binding.tvPrice.setTextColor(notSaleColor);
                    binding.tvSalePrice.setVisibility(View.GONE);
                }
            }
        });
    }

    private void updatePositionText(int position) {
        String value = (position + 1) + "/" + productSize;
        binding.tvPosition.setText(value);
        binding.tvShortDesc.setText(productVariantList.get(position).getDesc());
    }

    private void showBottomSheet(String tag) {
        ChooseProductFragment chooseProductFragment = new ChooseProductFragment();
        chooseProductFragment.show(getSupportFragmentManager(), tag);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
