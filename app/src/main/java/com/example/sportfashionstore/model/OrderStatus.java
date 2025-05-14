package com.example.sportfashionstore.model;

import java.io.Serializable;

public class OrderStatus implements Serializable {
    private String desc;
    private int textColor;

    public OrderStatus(String desc, int textColor) {
        this.desc = desc;
        this.textColor = textColor;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }
}
