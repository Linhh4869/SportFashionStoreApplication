package com.example.sportfashionstore.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.sportfashionstore.app.MyApplication;
import com.example.sportfashionstore.callback.DataStateCallback;
import com.example.sportfashionstore.commonbase.BaseViewModel;
import com.example.sportfashionstore.commonbase.Resource;
import com.example.sportfashionstore.data.entity.AddressEntity;
import com.example.sportfashionstore.data.entity.CartEntity;
import com.example.sportfashionstore.model.InfoPayment;
import com.example.sportfashionstore.model.Order;
import com.example.sportfashionstore.model.ProductVariant;
import com.example.sportfashionstore.model.StripePaymentModel;
import com.example.sportfashionstore.model.User;
import com.example.sportfashionstore.repository.AddressRepository;
import com.example.sportfashionstore.repository.CartRepository;
import com.example.sportfashionstore.repository.ProductRepository;
import com.example.sportfashionstore.repository.StripeRepository;
import com.example.sportfashionstore.util.Constants;
import com.example.sportfashionstore.util.SharePrefHelper;
import com.google.gson.Gson;
import com.stripe.android.paymentsheet.PaymentSheet;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;

public class CheckoutViewModel extends BaseViewModel {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final AddressRepository addressRepository;
    private final StripeRepository stripeRepository;
    private final MutableLiveData<String> userName = new MutableLiveData<>("");
    private final MutableLiveData<String> address = new MutableLiveData<>("");
    private final MutableLiveData<CartEntity> cartEntityLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> enablePayButton = new MutableLiveData<>();
    private final MutableLiveData<AddressEntity> selectedAddress = new MutableLiveData<>(null);
    private final MutableLiveData<Resource<String>> saveOrderLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<AddressEntity>> allAddress = new MutableLiveData<>();
    private final MutableLiveData<String> nameLiveData = new MutableLiveData<>("");
    private final MutableLiveData<String> phoneLiveData = new MutableLiveData<>("");
    private final MutableLiveData<String> addressLiveData = new MutableLiveData<>("");
    private final MutableLiveData<Boolean> defaultAddressLiveData = new MutableLiveData<>(false);
    private final MutableLiveData<AddressEntity> editingAddress = new MutableLiveData<>();
    private final MediatorLiveData<Boolean> isButtonDialogEnabled = new MediatorLiveData<>();
    private final MediatorLiveData<Boolean> isEnableCheckoutButton = new MediatorLiveData<>(false);
    private final MutableLiveData<Unit> refreshEvent = new MutableLiveData<>();
    private final MutableLiveData<String> paymentClientSecretLiveData = new MutableLiveData<>();
    public MutableLiveData<Resource<PaymentSheet.CustomerConfiguration>> cusConfigLiveData = new MutableLiveData<>();
    private String uid = "";
    private PaymentSheet.CustomerConfiguration customerConfig;
    public MutableLiveData<Integer> paymentForm = new MutableLiveData<>(Constants.CASH_ON_DELIVERY);


    public CheckoutViewModel() {
        cartRepository = new CartRepository();
        productRepository = new ProductRepository();
        addressRepository = new AddressRepository();
        stripeRepository = new StripeRepository();
        SharePrefHelper sharePrefHelper = MyApplication.getSharePrefHelper();
        isButtonDialogEnabled.addSource(nameLiveData, value -> updateButtonState());
        isButtonDialogEnabled.addSource(phoneLiveData, value -> updateButtonState());
        isButtonDialogEnabled.addSource(addressLiveData, value -> updateButtonState());
        isEnableCheckoutButton.addSource(selectedAddress, value -> updateEnableCheckoutBtn());
        updateButtonState();
        updateEnableCheckoutBtn();
    }

    public List<InfoPayment> getInfoPaymentList() {
        if (cartEntityLiveData == null || cartEntityLiveData.getValue() == null) {
            return new ArrayList<>();
        }
        CartEntity cart = cartEntityLiveData.getValue();
        int mPrice = (int) (cart.getSalePrice() > 0 ? cart.getSalePrice() : cart.getPrice());
        int totalPrice = getTotalPrice();
        List<InfoPayment> infoPaymentList = new ArrayList<>();
        infoPaymentList.add(new InfoPayment("Giá sản phẩm", mPrice));
        if (cart.getSalePrice() > 0) {
            int totalSalePrice = (int) (cart.getPrice() * cart.getQuantity() - totalPrice);
            infoPaymentList.add(new InfoPayment("Tổng giá giảm", -totalSalePrice));
        }
        infoPaymentList.add(new InfoPayment("Phí giao hàng", 15000));
        infoPaymentList.add(new InfoPayment("Tổng số tiền", totalPrice));

        return infoPaymentList;
    }

    public int getTotalPrice() {
        if (cartEntityLiveData == null || cartEntityLiveData.getValue() == null) {
            return 0;
        }

        CartEntity cart = cartEntityLiveData.getValue();
        int mPrice = (int) (cart.getSalePrice() > 0 ? cart.getSalePrice() : cart.getPrice());
        int allProductPrice = mPrice * cart.getQuantity();
        return allProductPrice + 15000;
    }

    public void getInfoPayment(long id) {
        cartRepository.getCartItemById(id, this::setCartEntityLiveData);
    }

    public void getChooseAddress() {
        addressRepository.getSelectedAddress(new DataStateCallback<>() {
            @Override
            public void onSuccess(AddressEntity data) {
                setSelectedAddress(data);
            }

            @Override
            public void onError(String message) {
                getAllAddressList();
            }
        });
    }

    public void getAllAddressList() {
        addressRepository.getAllAddressList(new DataStateCallback<>() {
            @Override
            public void onSuccess(List<AddressEntity> data) {
                allAddress.postValue(data);
                if (selectedAddress.getValue() == null) {
                    selectedAddress.postValue(data.get(0));
                }
            }

            @Override
            public void onError(String message) {
                allAddress.postValue(null);
            }
        });
    }

    public void updateSelectedAddress(AddressEntity address) {
        addressRepository.updateSelectedRecord();
        if (address.isDefaultAddress() == 1) {
            addressRepository.updateDefaultRecord();
        }
        addressRepository.updateAddress(address);
        getChooseAddress();
    }

    public void setDataUpdateDialog(AddressEntity address) {
        if (address == null) {
            nameLiveData.setValue("");
            phoneLiveData.setValue("");
            addressLiveData.setValue("");
            return;
        }

        nameLiveData.setValue(address.getName());
        phoneLiveData.setValue(address.getPhone());
        addressLiveData.setValue(address.getAddress());
        defaultAddressLiveData.setValue(address.isDefaultAddress() == 1);
    }

    private boolean isValidInfo(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private void updateButtonState() {
        boolean isEnabled = isValidInfo(nameLiveData.getValue()) &&
                isValidInfo(phoneLiveData.getValue()) &&
                isValidInfo(addressLiveData.getValue());
        isButtonDialogEnabled.postValue(isEnabled);
    }

    private void updateEnableCheckoutBtn() {
        boolean isEnable = selectedAddress.getValue() != null;
        isEnableCheckoutButton.postValue(isEnable);
    }

    public void deleteAddress(AddressEntity address) {
        addressRepository.deleteAddress(address);
        getAllAddressList();
    }

    public void updateAddress(AddressEntity address) {
        address.setName(nameLiveData.getValue());
        address.setPhone(phoneLiveData.getValue());
        address.setAddress(addressLiveData.getValue());
        address.setDefaultAddress(Boolean.TRUE.equals(defaultAddressLiveData.getValue()) ? 1 : 0);
        addressRepository.updateAddress(address);
        getAllAddressList();
    }

    public void addNewAddress() {
        int isDefault = Boolean.TRUE.equals(defaultAddressLiveData.getValue()) ? 1 : 0;
        AddressEntity newAddress = new AddressEntity(
                nameLiveData.getValue(),
                phoneLiveData.getValue(),
                addressLiveData.getValue(),
                isDefault,
                0);
        addressRepository.insertAddress(newAddress);
        getAllAddressList();
    }

    public void saveOrder(boolean isPaid) {
        if (cartEntityLiveData == null || cartEntityLiveData.getValue() == null)
            return;

        CartEntity cartEntity = cartEntityLiveData.getValue();
        Order order = cartEntity.convertToOrder();
        User user = new User();
        AddressEntity selectedAdd = selectedAddress.getValue();
        if (selectedAdd == null) return;
        user.setDisplayName(selectedAdd.getName());
        user.setAddress(selectedAdd.getAddress());
        user.setPhoneNumber(selectedAdd.getPhone());
        String userInfo = new Gson().toJson(user);
        order.setCustomerInfo(userInfo);
        order.setTotalPrice(getTotalPrice());
        order.setStatus(0);
        order.setPaymentMethod(isPaid ? Constants.PAY_WITH_CREDIT : Constants.CASH_ON_DELIVERY);

        setLoadingState(saveOrderLiveData);
        productRepository.getProductVariantById(cartEntity.getProductVariantId(), new DataStateCallback<>() {
            @Override
            public void onSuccess(ProductVariant data) {
                try {
                    int currentQuantity = Integer.parseInt(data.getInventory());
                    if (currentQuantity < cartEntity.getQuantity()) {
                        setErrorState(saveOrderLiveData, "Số lượng sản phẩm đặt hàng vượt quá số lượng sản phẩm trong kho!");
                        return;
                    }

                    int updateQuantity = currentQuantity - cartEntity.getQuantity();
                    updateOrder(order, updateQuantity, data.getId());
                } catch (Exception e) {
                    setErrorState(saveOrderLiveData, e.getMessage());
                }
            }

            @Override
            public void onError(String message) {
                setErrorState(saveOrderLiveData, message);
            }
        });
    }

    private void updateOrder(Order order, int updateQuantity, String variantId) {
        cartRepository.addNewOrder(order, updateQuantity, variantId, new DataStateCallback<>() {
            @Override
            public void onSuccess(String data) {
                setSuccessState(saveOrderLiveData, data);
                clearData();
            }

            @Override
            public void onError(String message) {
                setErrorState(saveOrderLiveData, message);
            }
        });
    }

    public void prepareShowPaymentSheet() {
        setLoadingState(cusConfigLiveData);
        stripeRepository.getCustomer(new DataStateCallback<StripePaymentModel>() {
            @Override
            public void onSuccess(StripePaymentModel data) {
                if (data == null) {
                    setErrorState(cusConfigLiveData, "Khong tao duoc customer");
                    return;
                }
                uid = data.getId();
                getEphemeralKey();
            }

            @Override
            public void onError(String message) {
                setErrorState(cusConfigLiveData, message);
            }
        });
    }

    private void getEphemeralKey() {
        stripeRepository.createEphemeralKey(uid, new DataStateCallback<>() {
            @Override
            public void onSuccess(StripePaymentModel data) {
                if (data == null) {
                    setErrorState(cusConfigLiveData, "Loi roi");
                    return;
                }
                customerConfig = new PaymentSheet.CustomerConfiguration(uid, data.getEphemeralKey());
                getPaymentIntent();
            }

            @Override
            public void onError(String message) {
                setErrorState(cusConfigLiveData, message);
            }
        });
    }

    private void getPaymentIntent() {
        stripeRepository.createPaymentIntent(uid, getTotalPrice(), Constants.CURRENCY, new DataStateCallback<>() {
            @Override
            public void onSuccess(StripePaymentModel data) {
                if (data == null) {
                    setErrorState(cusConfigLiveData, "Loi roi");
                    return;
                }
                paymentClientSecretLiveData.setValue(data.getClientSecret());
                setSuccessState(cusConfigLiveData, customerConfig);
            }

            @Override
            public void onError(String message) {
                setErrorState(cusConfigLiveData, "");
            }
        });
    }

    public void clearData() {
        if (cartEntityLiveData == null || cartEntityLiveData.getValue() == null
                || cartEntityLiveData.getValue().isShowCart() == 0) {
            cartRepository.deleteCartItemNotCart();
            return;
        }

        cartRepository.deleteCartItem(cartEntityLiveData.getValue());

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

    public MutableLiveData<AddressEntity> getSelectedAddress() {
        return selectedAddress;
    }

    public void setSelectedAddress(AddressEntity address) {
        selectedAddress.postValue(address);
    }

    public MutableLiveData<List<AddressEntity>> getAllAddress() {
        return allAddress;
    }

    public MutableLiveData<String> getNameLiveData() {
        return nameLiveData;
    }

    public MutableLiveData<String> getPhoneLiveData() {
        return phoneLiveData;
    }

    public MutableLiveData<String> getAddressLiveData() {
        return addressLiveData;
    }

    public MutableLiveData<Boolean> getDefaultAddressLiveData() {
        return defaultAddressLiveData;
    }

    public void setDefaultAddressLiveData(boolean isDefault) {
        defaultAddressLiveData.postValue(isDefault);
    }

    public MediatorLiveData<Boolean> getIsButtonDialogEnabled() {
        return isButtonDialogEnabled;
    }

    public MutableLiveData<AddressEntity> getEditingAddress() {
        return editingAddress;
    }

    public void setEditingAddress(AddressEntity address) {
        editingAddress.setValue(address);
    }

    public LiveData<Unit> getRefreshEvent() {
        return refreshEvent;
    }

    public void triggerRefresh() {
        refreshEvent.setValue(Unit.INSTANCE);
    }

    public MutableLiveData<Resource<PaymentSheet.CustomerConfiguration>> getCusConfigLiveData() {
        return cusConfigLiveData;
    }

    public String getPaymentClientSecretLiveData() {
        return paymentClientSecretLiveData.getValue();
    }

    public MutableLiveData<Integer> getPaymentForm() {
        return paymentForm;
    }

    public void setPaymentForm(int paymentForm) {
        this.paymentForm.setValue(paymentForm);
    }
}
