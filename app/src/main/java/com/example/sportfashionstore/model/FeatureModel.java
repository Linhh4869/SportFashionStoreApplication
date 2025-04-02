package com.example.sportfashionstore.model;

import com.example.sportfashionstore.util.Constants;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FeatureModel implements Serializable {
    @SerializedName("label")
    private String label;

    @SerializedName("icon")
    private int icon;

    private Constants.Setting typeSetting;

    public FeatureModel(String label, int icon, Constants.Setting typeSetting) {
        this.label = label;
        this.icon = icon;
        this.typeSetting = typeSetting;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Constants.Setting getTypeSetting() {
        return typeSetting;
    }

    public void setTypeSetting(Constants.Setting typeSetting) {
        this.typeSetting = typeSetting;
    }
}
