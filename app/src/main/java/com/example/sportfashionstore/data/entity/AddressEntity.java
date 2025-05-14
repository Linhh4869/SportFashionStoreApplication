package com.example.sportfashionstore.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "address")
public class AddressEntity implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private String name;

    private String phone;

    private String address;

    private int isDefaultAddress;

    private int isSelected;

    public AddressEntity(String name, String phone, String address, int isDefaultAddress, int isSelected) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.isDefaultAddress = isDefaultAddress;
        this.isSelected = isSelected;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int isDefaultAddress() {
        return isDefaultAddress;
    }

    public void setDefaultAddress(int defaultAddress) {
        isDefaultAddress = defaultAddress;
    }

    public int isSelected() {
        return isSelected;
    }

    public void setSelected(int selected) {
        isSelected = selected;
    }
}
