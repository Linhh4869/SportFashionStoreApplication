package com.example.sportfashionstore.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.example.sportfashionstore.callback.DataStateCallback;
import com.example.sportfashionstore.commonbase.BaseViewModel;
import com.example.sportfashionstore.commonbase.Resource;
import com.example.sportfashionstore.model.Product;
import com.example.sportfashionstore.model.ProductVariant;
import com.example.sportfashionstore.model.SizeModel;
import com.example.sportfashionstore.repository.ProductRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ChooseProductViewModel extends BaseViewModel {
    private final ProductRepository productRepository;
    private final MutableLiveData<Resource<Product>> productLiveData = new MutableLiveData<>();
    private final MutableLiveData<Resource<List<ProductVariant>>> variantLiveData = new MutableLiveData<>();
    private final ArrayList<String> defaultSize = new ArrayList<>(Arrays.asList("M", "L", "XL", "2XL", "3XL"));


    public ChooseProductViewModel() {
        productRepository = new ProductRepository();
    }

    public void getProductById(String productId) {
        setLoadingState(productLiveData);
        productRepository.getProductById(productId, new DataStateCallback<>() {
            @Override
            public void onSuccess(Product data) {
                setSuccessState(productLiveData, data);
                getVariantList(productId);
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    public void getVariantList(String productId) {
        setLoadingState(variantLiveData);
        productRepository.getProductVariants(productId, new DataStateCallback<>() {
            @Override
            public void onSuccess(List<ProductVariant> data) {
                Product mProduct = Objects.requireNonNull(productLiveData.getValue()).data;
                mProduct.setProductVariants(data);
                setSuccessState(productLiveData, mProduct);
                setSuccessState(variantLiveData, data);
            }

            @Override
            public void onError(String message) {
                setErrorState(variantLiveData, message);
            }
        });
    }

    private List<SizeModel> getSizeList() {
        if (variantLiveData.getValue() == null || variantLiveData.getValue().data == null) {
            return new ArrayList<>();
        }

        List<ProductVariant> variants = variantLiveData.getValue().data;
        Set<String> mergedSet = new HashSet<>();
        for (ProductVariant variant : variants) {
            mergedSet.addAll(variant.getSize());
        }

        List<String> mergedList = new ArrayList<>(mergedSet);
        List<SizeModel> result = new ArrayList<>();
        for (String size : defaultSize) {
            if (mergedList.contains(size)) {
                result.add(new SizeModel(size, false));
            }
        }

        return result;
    }

    public List<SizeModel> setStateSize(ProductVariant variant) {
        List<SizeModel> currentList = getSizeList();

        for (SizeModel sizeModel : currentList) {
            sizeModel.setAvailable(variant.getSize().contains(sizeModel.getSize()));
        }

        return currentList;
    }

    public MutableLiveData<Resource<List<ProductVariant>>> getVariantsLiveData() {
        return variantLiveData;
    }

    public MutableLiveData<Resource<Product>> getProductLiveData() {
        return productLiveData;
    }
}
