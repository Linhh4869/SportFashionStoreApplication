package com.example.sportfashionstore.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.example.sportfashionstore.ui.commonbase.BaseViewModel;
import com.example.sportfashionstore.ui.commonbase.SingleLiveData;
import com.example.sportfashionstore.model.ProductVariant;
import com.example.sportfashionstore.model.SizeModel;
import com.example.sportfashionstore.util.Helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class VariantManagementViewModel extends BaseViewModel {
    private final ArrayList<String> defaultSize = new ArrayList<>(Arrays.asList("M", "L", "XL", "2XL", "3XL"));
    private final MutableLiveData<ProductVariant> submitVariant = new MutableLiveData<>(new ProductVariant());
    private MutableLiveData<List<String>> submitSizeList = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> isValidColor = new MutableLiveData<>(true);
    private final MutableLiveData<Boolean> isValidQuantity = new MutableLiveData<>(true);
    private final MutableLiveData<Boolean> isValidSizeList = new MutableLiveData<>(true);
    private final MutableLiveData<Boolean> isValidImage = new MutableLiveData<>(true);
    private final MutableLiveData<List<SizeModel>> sizeListSelected = new MutableLiveData<>();
    private SingleLiveData<ProductVariant> resultVariant = new SingleLiveData<>();

    @Inject
    public VariantManagementViewModel() {

    }

    public List<SizeModel> getAllSize() {
        List<SizeModel> allSize = new ArrayList<>();
        for (String size : defaultSize) {
            allSize.add(new SizeModel(false, size));
        }

        sizeListSelected.setValue(allSize);
        return allSize;
    }

    public List<SizeModel> getAllSizeOfVariant(List<String> currentSizeList) {
        List<SizeModel> allSizeOfVariant = getAllSize();
        for (SizeModel sizeModel : allSizeOfVariant) {
            if (currentSizeList.contains(sizeModel.getSize())) {
                sizeModel.setSelected(true);
            }
        }

        sizeListSelected.setValue(allSizeOfVariant);
        return allSizeOfVariant;
    }

    public void onSubmitVariant() {
        if (isValidSubmitVariant()) {
            if (submitVariant.getValue() != null && submitVariant.getValue().getId().isEmpty()) {
                submitVariant.getValue().setId(UUID.randomUUID().toString());
            }
            resultVariant.postValue(submitVariant.getValue());
        }
    }

    public boolean isValidSubmitVariant() {
        if (submitVariant == null || submitVariant.getValue() == null)
            return false;

        return checkValidColor() && checkValidQuantity() && checkValidSizeList() && checkValidImage();
    }

    private boolean checkValidColor() {
        String color = submitVariant.getValue().getDesc();
        isValidColor.setValue(Helper.isValidContent(color, 4));
        return Boolean.TRUE.equals(isValidColor.getValue());
    }

    private boolean checkValidQuantity() {
        int quantity = Integer.parseInt(submitVariant.getValue().getInventory());
        isValidQuantity.setValue(quantity > 0);
        return Boolean.TRUE.equals(isValidQuantity.getValue());
    }

    private boolean checkValidSizeList() {
        List<String> sizeList = new ArrayList<>();
        for (SizeModel sizeModel : getSizeListSelected()) {
            if (sizeModel.isSelected() && !sizeModel.getSize().isEmpty())
                sizeList.add(sizeModel.getSize());
        }
        isValidSizeList.setValue(!sizeList.isEmpty());
        submitVariant.getValue().setSize(sizeList);
        return Boolean.TRUE.equals(isValidSizeList.getValue());
    }

    private boolean checkValidImage() {
        String url = submitVariant.getValue().getImage();
        isValidImage.setValue(Helper.isValidContent(url, 15));
        return Boolean.TRUE.equals(isValidImage.getValue());
    }


    public ProductVariant getSubmitVariant() {
        return submitVariant.getValue();
    }

    public void setSubmitVariant(ProductVariant submitVariant) {
        this.submitVariant.setValue(submitVariant);
    }

    public List<String> getSubmitSizeList() {
        return submitSizeList.getValue();
    }

    public void setSubmitSizeList(MutableLiveData<List<String>> submitSizeList) {
        this.submitSizeList = submitSizeList;
    }

    public SingleLiveData<ProductVariant> getResultVariant() {
        return resultVariant;
    }

    public void setResultVariant(SingleLiveData<ProductVariant> resultVariant) {
        this.resultVariant = resultVariant;
    }

    public List<SizeModel> getSizeListSelected() {
        return sizeListSelected.getValue();
    }

    public MutableLiveData<Boolean> getIsValidColor() {
        return isValidColor;
    }

    public MutableLiveData<Boolean> getIsValidQuantity() {
        return isValidQuantity;
    }

    public MutableLiveData<Boolean> getIsValidSizeList() {
        return isValidSizeList;
    }

    public MutableLiveData<Boolean> getIsValidImage() {
        return isValidImage;
    }
}
