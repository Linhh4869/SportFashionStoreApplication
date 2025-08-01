package com.example.sportfashionstore.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.example.sportfashionstore.ui.commonbase.BaseViewModel;
import com.example.sportfashionstore.callback.DataStateCallback;
import com.example.sportfashionstore.ui.commonbase.Resource;
import com.example.sportfashionstore.model.Product;
import com.example.sportfashionstore.repository.ProductRepository;
import com.example.sportfashionstore.util.SharePrefHelper;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class HomeViewModel extends BaseViewModel {
    private final ProductRepository productRepository;
    private final SharePrefHelper sharePrefHelper;
    private final MutableLiveData<Resource<List<Product>>> productLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Product>> productList = new MutableLiveData<>(null);
    private final MutableLiveData<String> userName = new MutableLiveData<>("");

    @Inject
    public HomeViewModel(ProductRepository productRepository, SharePrefHelper sharePrefHelper) {
        this.productRepository = productRepository;
        this.sharePrefHelper = sharePrefHelper;
        userName.setValue(this.sharePrefHelper.getUserName());
    }

    public void getProductListHome() {
        setLoadingState(productLiveData);
        productRepository.getProducts(new DataStateCallback<>() {
            @Override
            public void onSuccess(List<Product> data) {
                setSuccessState(productLiveData, data);
                productList.setValue(data);
            }

            @Override
            public void onError(String message) {
                setErrorState(productLiveData, message);
                productList.setValue(null);
            }
        });
    }

    public String getCurrentRole() {
        return this.sharePrefHelper.getRole();
    }

    public MutableLiveData<String> getUserName() {
        return userName;
    }

    public void onUserNameChange(String newName) {
        userName.setValue(newName);
    }

    public MutableLiveData<Resource<List<Product>>> getProductLiveData() {
        return productLiveData;
    }

    public MutableLiveData<List<Product>> getProductList() {
        return productList;
    }
}
