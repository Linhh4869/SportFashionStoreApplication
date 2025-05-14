package com.example.sportfashionstore.repository;

import androidx.lifecycle.LiveData;

import com.example.sportfashionstore.app.MyApplication;
import com.example.sportfashionstore.callback.DataStateCallback;
import com.example.sportfashionstore.data.AppDatabase;
import com.example.sportfashionstore.data.dao.AddressDao;
import com.example.sportfashionstore.data.entity.AddressEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddressRepository {
    private final AddressDao addressDao;
    private final LiveData<List<AddressEntity>> allAddress;
    private final ExecutorService executorService;

    public AddressRepository() {
        AppDatabase database = MyApplication.getAppDatabase();
        addressDao = database.addressDao();
        allAddress = addressDao.getAllAddress();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<AddressEntity>> getAllAddress() {
        return allAddress;
    }

    public void insertAddress(final AddressEntity address) {
        executorService.execute(() -> addressDao.insertAddress(address));
    }

    public void updateAddress(final AddressEntity address) {
        executorService.execute(() -> addressDao.updateAddress(address));
    }

    public void deleteAddress(final AddressEntity address) {
        executorService.execute(() -> addressDao.deleteAddress(address));
    }

    public void deleteAddressById(long id) {
        executorService.execute(() -> addressDao.deleteAddressById(id));
    }

    public void getSelectedAddress(DataStateCallback<AddressEntity> callback) {
        executorService.execute(() -> {
            AddressEntity selectedAddress = addressDao.getSelectedAddress();
            if (selectedAddress != null) {
                callback.onSuccess(selectedAddress);
            } else {
                callback.onError("");
            }
        });
    }

    public void getAddressById(long id, DataStateCallback<AddressEntity> callback) {
        executorService.execute(() -> {
            AddressEntity address = addressDao.getAddressById(id);
            if (address != null) {
                callback.onSuccess(address);
            } else {
                callback.onError("");
            }
        });
    }

    public void getAllAddressList(DataStateCallback<List<AddressEntity>> callback) {
        executorService.execute(() -> {
            List<AddressEntity> allAddressList = addressDao.getAllAddressList();
            if (allAddressList != null && !allAddressList.isEmpty()) {
                callback.onSuccess(allAddressList);
            } else {
                callback.onError("");
            }
        });
    }

    public void getDefaultAddress(DataStateCallback<AddressEntity> callback) {
        executorService.execute(() -> {
            AddressEntity defaultAddress = addressDao.getDefaultAddress();
            if (defaultAddress != null) {
                callback.onSuccess(defaultAddress);
            } else {
                callback.onError("");
            }
        });
    }

    public void updateSelectedRecord() {
        executorService.execute(addressDao::updateSelectedRecord);
    }

    public void updateDefaultRecord() {
        executorService.execute(addressDao::updateDefaultRecord);
    }
}
