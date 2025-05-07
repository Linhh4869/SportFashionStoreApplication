package com.example.sportfashionstore.model;

import java.io.Serializable;

public class InfoPayment implements Serializable {
    private String label;
    private Integer value;

    public InfoPayment(String label, Integer value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label != null ? label : "";
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getValue() {
        return value != null? value : 0;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
