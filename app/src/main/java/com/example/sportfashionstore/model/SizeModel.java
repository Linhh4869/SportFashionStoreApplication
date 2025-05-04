package com.example.sportfashionstore.model;

import java.io.Serializable;

public class SizeModel implements Serializable {
    private String size;

    private boolean isAvailable = false;

    public SizeModel(String size, boolean isAvailable) {
        this.size = size;
        this.isAvailable = isAvailable;
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
}
