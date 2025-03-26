package com.example.sportfashionstore.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FeatureModel implements Serializable {
    @SerializedName("label")
    private String label;

    @SerializedName("icon")
    private int icon;

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
}
