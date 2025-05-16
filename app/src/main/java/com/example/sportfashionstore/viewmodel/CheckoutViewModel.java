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
import com.example.sportfashionstore.model.User;
import com.example.sportfashionstore.repository.AddressRepository;
import com.example.sportfashionstore.repository.CartRepository;
import com.example.sportfashionstore.repository.ProductRepository;
import com.example.sportfashionstore.util.SharePrefHelper;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class CheckoutViewModel extends BaseViewModel {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final AddressRepository addressRepository;
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

    public CheckoutViewModel() {
        cartRepository = new CartRepository();
        productRepository = new ProductRepository();
        addressRepository = new AddressRepository();
        SharePrefHelper sharePrefHelper = MyApplication.getSharePrefHelper();
        userName.setValue(sharePrefHelper.getUserName());
        address.setValue(sharePrefHelper.getAddress());
        isButtonDialogEnabled.addSource(nameLiveData, value -> updateButtonState());
        isButtonDialogEnabled.addSource(phoneLiveData, value -> updateButtonState());
        isButtonDialogEnabled.addSource(addressLiveData, value -> updateButtonState());
        isEnableCheckoutButton.addSource(selectedAddress, value -> updateEnableCheckoutBtn());
        updateButtonState();
        updateEnableCheckoutBtn();
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

    public void deleteAddress(long id) {
        addressRepository.deleteAddressById(id);
        getAllAddressList();
    }

    public void updateAddress(long id) {
        addressRepository.getAddressById(id, new DataStateCallback<AddressEntity>() {
            @Override
            public void onSuccess(AddressEntity data) {
                int isDefault = Boolean.TRUE.equals(defaultAddressLiveData.getValue()) ? 1 : 0;
                data.setName(nameLiveData.getValue());
                data.setAddress(addressLiveData.getValue());
                data.setPhone(phoneLiveData.getValue());
                data.setDefaultAddress(isDefault);
                addressRepository.updateAddress(data);
                getAllAddressList();
            }

            @Override
            public void onError(String message) {

            }
        });
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

    public void saveOrder(CartEntity cartEntity, int totalPrice) {
        Order order = cartEntity.convertToOrder();
        User user = new User();
        AddressEntity selectedAdd = selectedAddress.getValue();
        if (selectedAdd == null) return;
        user.setDisplayName(selectedAdd.getName());
        user.setAddress(selectedAdd.getAddress());
        user.setPhoneNumber(selectedAdd.getPhone());
        String userInfo = new Gson().toJson(user);
        order.setCustomerInfo(userInfo);
        order.setTotalPrice(totalPrice);

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

    public void clearData() {
        cartRepository.deleteCartItemById();
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
}
