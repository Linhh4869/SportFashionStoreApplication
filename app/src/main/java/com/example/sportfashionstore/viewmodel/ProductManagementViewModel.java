package com.example.sportfashionstore.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.example.sportfashionstore.app.MyApplication;
import com.example.sportfashionstore.callback.DataStateCallback;
import com.example.sportfashionstore.commonbase.BaseViewModel;
import com.example.sportfashionstore.commonbase.Resource;
import com.example.sportfashionstore.model.Category;
import com.example.sportfashionstore.model.Product;
import com.example.sportfashionstore.model.ProductVariant;
import com.example.sportfashionstore.repository.ProductManagementRepository;

import java.util.ArrayList;
import java.util.List;

public class ProductManagementViewModel extends BaseViewModel {
    private final ProductManagementRepository productMnRepo;
    public static final String ALL_PRODUCT = "";
    private MutableLiveData<String> userName = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Category>> categoryList = new MutableLiveData<>();
    private MutableLiveData<Resource<List<Product>>> productsLiveData = new MutableLiveData<>();
    private MutableLiveData<Resource<Product>> productLiveData = new MutableLiveData<>();
    private MutableLiveData<List<ProductVariant>> variantLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> price = new MutableLiveData<>();
    private MutableLiveData<Integer> salePrice = new MutableLiveData<>(0);
    private MutableLiveData<Category> selectedCategory = new MutableLiveData<>();
    private MutableLiveData<String> descProduct = new MutableLiveData<>();
    private MutableLiveData<String> colorVariant = new MutableLiveData<>();
    private MutableLiveData<Integer> invVariant = new MutableLiveData<>();
    private MutableLiveData<List<String>> sizeList = new MutableLiveData<>();
    private MutableLiveData<String> urlImage = new MutableLiveData<>();

    public ProductManagementViewModel() {
        productMnRepo = new ProductManagementRepository();
        userName.setValue(MyApplication.getSharePrefHelper().getUserName());
    }

    public MutableLiveData<String> getUserName() {
        return userName;
    }

    public void setUserName(MutableLiveData<String> userName) {
        this.userName = userName;
    }

    public void getAllCategoryList() {
        setLoadingState(productsLiveData);
        productMnRepo.getCategoryList(new DataStateCallback<>() {
            @Override
            public void onSuccess(ArrayList<Category> data) {
                categoryList.setValue(data);
                getProductsByCategory(ALL_PRODUCT);
            }

            @Override
            public void onError(String message) {
                setErrorState(productsLiveData, message);
            }
        });
    }

    public void getProductsByCategory(String categoryId) {
        setLoadingState(productsLiveData);
        productMnRepo.getProductListByCategory(categoryId, new DataStateCallback<>() {
            @Override
            public void onSuccess(List<Product> data) {
                setSuccessState(productsLiveData, data);
            }

            @Override
            public void onError(String message) {
                setErrorState(productsLiveData, message);
            }
        });
    }

    public void getProductToCURD(String productId) {
        setLoadingState(productLiveData);
        productMnRepo.getProductToCURD(productId, new DataStateCallback<>() {
            @Override
            public void onSuccess(Product data) {
                setSuccessState(productLiveData, data);
            }

            @Override
            public void onError(String message) {
                setErrorState(productLiveData, message);
            }
        });
    }

    public List<String> getCategoryString(List<Category> list) {
        List<String> categories = new ArrayList<>();
        for (Category category : list) {
            categories.add(category.getName());
        }

        return categories;
    }

    public MutableLiveData<ArrayList<Category>> getCategoryList() {
        return categoryList;
    }

    public MutableLiveData<Resource<List<Product>>> getProductsLiveData() {
        return productsLiveData;
    }

    public MutableLiveData<Resource<Product>> getProductLiveData() {
        return productLiveData;
    }

    public MutableLiveData<List<ProductVariant>> getVariantLiveData() {
        return variantLiveData;
    }

    public void setVariantLiveData(List<ProductVariant> variantLiveData) {
        this.variantLiveData.setValue(variantLiveData);
    }

    public MutableLiveData<String> getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(MutableLiveData<String> urlImage) {
        this.urlImage = urlImage;
    }

    public MutableLiveData<List<String>> getSizeList() {
        return sizeList;
    }

    public void setSizeList(MutableLiveData<List<String>> sizeList) {
        this.sizeList = sizeList;
    }

    public MutableLiveData<Integer> getInvVariant() {
        return invVariant;
    }

    public void setInvVariant(MutableLiveData<Integer> invVariant) {
        this.invVariant = invVariant;
    }

    public MutableLiveData<String> getColorVariant() {
        return colorVariant;
    }

    public void setColorVariant(MutableLiveData<String> colorVariant) {
        this.colorVariant = colorVariant;
    }

    public MutableLiveData<String> getDescProduct() {
        return descProduct;
    }

    public void setDescProduct(MutableLiveData<String> descProduct) {
        this.descProduct = descProduct;
    }

    public MutableLiveData<Category> getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(MutableLiveData<Category> selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public MutableLiveData<Integer> getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Integer salePrice) {
        this.salePrice.postValue(salePrice);
    }

    public MutableLiveData<Integer> getPrice() {
        return price;
    }

    public String getPriceDisplay() {
        return String.valueOf(price.getValue());
    }

    public void setPriceDisplay(String price) {
        if (price != null && !price.isEmpty()) {
            try {
                this.price.setValue(Integer.valueOf(price));
            } catch (Exception e) {
                this.price.setValue(0);
            }
        } else {
            this.price.setValue(0);
        }
    }

    public void setPrice(MutableLiveData<Integer> price) {
        this.price = price;
    }

    public String getSalePriceDisplay() {
        return String.valueOf(salePrice.getValue());
    }

    public void setSalePriceDisplay(String price) {
        this.salePrice.setValue(Integer.valueOf(price));
    }
}
