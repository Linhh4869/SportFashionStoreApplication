package com.example.sportfashionstore.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.sportfashionstore.app.MyApplication;
import com.example.sportfashionstore.callback.DataStateCallback;
import com.example.sportfashionstore.commonbase.BaseViewModel;
import com.example.sportfashionstore.commonbase.Resource;
import com.example.sportfashionstore.commonbase.SingleLiveData;
import com.example.sportfashionstore.data.entity.CartEntity;
import com.example.sportfashionstore.model.InfoPayment;
import com.example.sportfashionstore.model.Order;
import com.example.sportfashionstore.model.User;
import com.example.sportfashionstore.repository.CartRepository;
import com.example.sportfashionstore.util.SharePrefHelper;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class CheckoutViewModel extends BaseViewModel {
    private final CartRepository cartRepository;
    private final MutableLiveData<String> userName = new MutableLiveData<>("");
    private final MutableLiveData<String> address = new MutableLiveData<>("");
    private final MutableLiveData<CartEntity> cartEntityLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> enablePayButton = new MutableLiveData<>();
    private final MutableLiveData<Resource<String>> saveOrderLiveData = new MutableLiveData<>();

    public CheckoutViewModel() {
        cartRepository = new CartRepository();
        SharePrefHelper sharePrefHelper = MyApplication.getSharePrefHelper();
        userName.setValue(sharePrefHelper.getUserName());
        address.setValue(sharePrefHelper.getAddress());
    }

    public List<InfoPayment> getInfoPaymentList(int price, int totalPrice) {
        List<InfoPayment> infoPaymentList = new ArrayList<>();
        infoPaymentList.add(new InfoPayment("Giá sản phẩm", price));
        infoPaymentList.add(new InfoPayment("Phí giao hàng", 15000));
        infoPaymentList.add(new InfoPayment("Tổng số tiền", totalPrice));

        return infoPaymentList;
    }

    public void getInfoPayment(long id) {
        cartRepository.getCartItemById(id, this::setCartEntityLiveData);
    }

    public void saveOrder(CartEntity cartEntity) {
        Order order = cartEntity.convertToOrder();
        User user = new User();
        user.setDisplayName(getUserName().getValue());
        user.setAddress(getAddress().getValue());
        String userInfo = new Gson().toJson(user);
        order.setCustomerInfo(userInfo);

        setLoadingState(saveOrderLiveData);
        cartRepository.addNewOrder(order, new DataStateCallback<>() {
            @Override
            public void onSuccess(String data) {
                setSuccessState(saveOrderLiveData, data);
                clearData(cartEntity.getId());
            }

            @Override
            public void onError(String message) {
                setErrorState(saveOrderLiveData, message);
            }
        });
    }

    public void clearData(long id) {
        cartRepository.deleteCartItemById(id);
    }

    public LiveData<CartEntity> getCartEntityLiveData() {
        return cartEntityLiveData;
    }

    public void setCartEntityLiveData(CartEntity cartEntityLiveData) {
        enablePayButton.postValue(cartEntityLiveData != null);
        this.cartEntityLiveData.postValue(cartEntityLiveData);
    }

    public MutableLiveData<String> getUserName() {
        return userName;
    }

    public MutableLiveData<String> getAddress() {
        return address;
    }

    public MutableLiveData<Boolean> getEnablePayButton() {
        return enablePayButton;
    }

    public MutableLiveData<Resource<String>> getSaveOrderLiveData() {
        return saveOrderLiveData;
    }
}
