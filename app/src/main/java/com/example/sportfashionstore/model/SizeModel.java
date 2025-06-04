package com.example.sportfashionstore.model;

import java.io.Serializable;

public class SizeModel implements Serializable {
    private String size;

    private boolean isAvailable;

    private boolean isSelected;

    private int position;

    public SizeModel(String size, boolean isAvailable) {
        this.size = size;
        this.isAvailable = isAvailable;
    }

    public SizeModel(boolean isSelected, String size) {
        this.size = size;
        this.isSelected = isSelected;
    }

    public String getSize() {
        return size != null ? size : "";
    }

    public void setSize(String size) {
        this.size = size;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
