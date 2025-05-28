package com.example.sportfashionstore.ui.fragment.owner;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.sportfashionstore.commonbase.BaseFragmentViewModel;
import com.example.sportfashionstore.commonbase.Resource;
import com.example.sportfashionstore.databinding.FragmentProductManagementBinding;
import com.example.sportfashionstore.model.Category;
import com.example.sportfashionstore.ui.CRUDProductActivity;
import com.example.sportfashionstore.ui.adapter.ProductHomeAdapter;
import com.example.sportfashionstore.util.Constants;
import com.example.sportfashionstore.viewmodel.ProductManagementViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ProductManagementFragment extends BaseFragmentViewModel<FragmentProductManagementBinding, ProductManagementViewModel> {
    private List<Category> categories = new ArrayList<>();
    private String categoriesJson = "";

    @Override
    protected FragmentProductManagementBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentProductManagementBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUi() {
        binding.setViewModel(viewModel);
        viewModel.getAllCategoryList(true);
        ProductHomeAdapter productHomeAdapter = new ProductHomeAdapter(item -> {
            Intent intent = new Intent(getActivity(), CRUDProductActivity.class);
            intent.putExtra(CRUDProductActivity.KEY_PRODUCT, item.getId());
            intent.putExtra(CRUDProductActivity.KEY_CURD, Constants.EDIT_PRODUCT);
//            intent.putExtra(CRUDProductActivity.KEY_CATEGORIES, categoriesJson);
            getActivity().startActivity(intent);
        });
        binding.rcvHomeProduct.setAdapter(productHomeAdapter);
        binding.btnAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CRUDProductActivity.class);
            intent.putExtra(CRUDProductActivity.KEY_CURD, Constants.EDIT_PRODUCT);
//            intent.putExtra(CRUDProductActivity.KEY_CATEGORIES, categoriesJson);
            getActivity().startActivity(intent);
        });
        binding.tlCategory.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (categories == null || categories.isEmpty()) {
                    return;
                }
                Category category = categories.get(tab.getPosition());
                viewModel.getProductsByCategory(category.getId());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (categories == null || categories.isEmpty()) {
                    return;
                }
                Category category = categories.get(tab.getPosition());
                viewModel.getProductsByCategory(category.getId());
            }
        });

        viewModel.getCategoryList().observe(this, cate -> {
            categories = viewModel.getCategoryList().getValue();
//            categoriesJson = new Gson().toJson(categories);
            assert categories != null;
            categories.add(0, new Category("Tất cả", ""));
            binding.tlCategory.removeAllTabs();
            for (Category category : categories) {
                if (category != null) {
                    TabLayout.Tab tab = binding.tlCategory.newTab();
                    tab.setText(category.getName());
                    binding.tlCategory.addTab(tab);
                }
            }
        });

        viewModel.getProductsLiveData().observe(this, resource -> {
            if (resource.state.equals(Resource.State.SUCCESS) && resource.data != null) {
                productHomeAdapter.setData(resource.data);
            } else {
                productHomeAdapter.setData(new ArrayList<>());
            }
        });

    }
}
