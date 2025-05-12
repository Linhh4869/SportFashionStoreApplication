package com.example.sportfashionstore.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.sportfashionstore.callback.DataStateCallback;
import com.example.sportfashionstore.commonbase.BaseViewModel;
import com.example.sportfashionstore.commonbase.Resource;
import com.example.sportfashionstore.commonbase.SingleLiveData;
import com.example.sportfashionstore.data.entity.CartEntity;
import com.example.sportfashionstore.model.Product;
import com.example.sportfashionstore.model.ProductVariant;
import com.example.sportfashionstore.model.SizeModel;
import com.example.sportfashionstore.repository.CartRepository;
import com.example.sportfashionstore.repository.ProductRepository;
import com.example.sportfashionstore.util.Constants;
import com.example.sportfashionstore.util.StringUtil;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ChooseProductViewModel extends BaseViewModel {
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final MutableLiveData<Resource<Product>> productLiveData = new MutableLiveData<>();
    private final MutableLiveData<Resource<List<ProductVariant>>> variantLiveData = new MutableLiveData<>();
    private final ArrayList<String> defaultSize = new ArrayList<>(Arrays.asList("M", "L", "XL", "2XL", "3XL"));
    private final MutableLiveData<String> _selectedSize = new MutableLiveData<>("");
    private MutableLiveData<Integer> _quantity = new MutableLiveData<>(1);
    private final MutableLiveData<ProductVariant> currentVariant = new MutableLiveData<>();
    private final MutableLiveData<Boolean> enablePayButton = new MutableLiveData<>(false);
    private int MAX_VALUE = 99;
    private final SingleLiveData<CartEntity> navigateToCheckout = new SingleLiveData<>();
    private final SingleLiveData<String> addToCart = new SingleLiveData<>();
    private final LiveData<List<CartEntity>> allCartItems;


    public ChooseProductViewModel() {
        productRepository = new ProductRepository();
        cartRepository = new CartRepository();
        allCartItems = cartRepository.getAllCartItems();
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
                setCurrentVariant(data.get(0));
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

    public void increaseQuantity() {
        Integer currentValue = _quantity.getValue();
        if (currentValue != null && currentValue < MAX_VALUE) {
            _quantity.setValue(currentValue + 1);
        }
    }

    public void decreaseQuantity() {
        Integer currentValue = _quantity.getValue();
        if (currentValue != null && currentValue > 1) {
            _quantity.setValue(currentValue - 1);
        }
    }

    public void handleButton(String tag) {
        if (productLiveData == null || productLiveData.getValue() == null || productLiveData.getValue().data == null
                || currentVariant.getValue() == null || _quantity.getValue() == null || !StringUtil.isNotNullAndEmpty(_selectedSize.getValue())) {
            return;
        }

        Product product = productLiveData.getValue().data;
        ProductVariant variant = currentVariant.getValue();
        int quantity = _quantity.getValue();
        String size = getSelectedSize();
        int isShowCart = tag.equals(Constants.ADD_CART) ? 1 : 0;

        CartEntity cartEntity = new CartEntity(
                product.getId(),
                variant.getProductVariantId(),
                product.getPrice(),
                product.getSalePrice(),
                size,
                product.isSaleClothes(),
                quantity,
                product.getDescription(),
                variant.getImage(),
                variant.getDesc(),
                Timestamp.now(),
                Timestamp.now(),
                isShowCart
        );

        long id = cartRepository.insertCartItem(cartEntity, getSelectedSize());

        if (tag.equals(Constants.PAY_NOW)) {
            cartEntity.setId(id);
            setDataCheckout(cartEntity);
        } else if (tag.equals(Constants.ADD_CART)) {
            onAddToCart();
        }
        resetState();
    }

    public void resetState() {
        _quantity.setValue(1);
        _selectedSize.setValue("");
        if (productLiveData.getValue() != null && !productLiveData.getValue().data.getProductVariants().isEmpty()) {
            ProductVariant variant = productLiveData.getValue().data.getProductVariants().get(0);
            setCurrentVariant(variant);
            setMAX_VALUE(Integer.parseInt(variant.getInventory()));
        }
    }

    public void deleteItemCart(CartEntity cartEntity) {
        cartRepository.deleteCartItem(cartEntity);
    }

    public MutableLiveData<Resource<List<ProductVariant>>> getVariantsLiveData() {
        return variantLiveData;
    }

    public MutableLiveData<Resource<Product>> getProductLiveData() {
        return productLiveData;
    }

    public String getSelectedSize() {
        return _selectedSize.getValue();
    }

    public void setSelectedSize(String size) {
        this._selectedSize.setValue(size);
    }

    public MutableLiveData<Integer> getQuantity() {
        return _quantity;
    }

    public void setQuantity(int quantity) {
        _quantity.postValue(quantity);
    }

    public MutableLiveData<ProductVariant> getCurrentVariant() {
        return currentVariant;
    }

    public void setCurrentVariant(ProductVariant variant) {
        this.currentVariant.postValue(variant);
    }

    public void setMAX_VALUE(int MAX_VALUE) {
        this.MAX_VALUE = MAX_VALUE;
    }

    public int getMAX_VALUE() {
        return MAX_VALUE;
    }

    public SingleLiveData<CartEntity> onNavigateToCheckout() {
        return navigateToCheckout;
    }

    public void setDataCheckout(CartEntity cartEntity) {
        navigateToCheckout.setValue(cartEntity);
    }

    public SingleLiveData<String> addToCartSuccess() {
        return addToCart;
    }

    public void onAddToCart() {
        addToCart.setValue("Thêm vào giỏ hàng thành công!");
    }

    public LiveData<List<CartEntity>> getAllCartItems() {
        return allCartItems;
    }

    public MutableLiveData<Boolean> getEnablePayButton() {
        return enablePayButton;
    }

    public void setEnableButton(boolean isEnable) {
        enablePayButton.postValue(isEnable);
    }
}
