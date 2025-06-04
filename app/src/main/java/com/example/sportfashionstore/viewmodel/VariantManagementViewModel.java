package com.example.sportfashionstore.viewmodel;

import com.example.sportfashionstore.commonbase.BaseViewModel;
import com.example.sportfashionstore.model.SizeModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VariantManagementViewModel extends BaseViewModel {
    private final ArrayList<String> defaultSize = new ArrayList<>(Arrays.asList("M", "L", "XL", "2XL", "3XL"));

    public List<SizeModel> getAllSize() {
        List<SizeModel> allSize = new ArrayList<>();
        for (String size : defaultSize) {
            allSize.add(new SizeModel(false, size));
        }

        return allSize;
    }

    public List<SizeModel> getAllSizeOfVariant(List<String> currentSizeList) {
        List<SizeModel> allSizeOfVariant = getAllSize();
        for (SizeModel sizeModel : allSizeOfVariant) {
            if (currentSizeList.contains(sizeModel.getSize())) {
                sizeModel.setSelected(true);
            }
        }

        return allSizeOfVariant;
    }
}
