package com.example.sportfashionstore.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "carts")
public class CartEntity implements Serializable {
    @PrimaryKey
    @SerializedName("userId")
    private String userId;

    @SerializedName("totalItems")
    private int totalItems;

    @SerializedName("totalPrices")
    private int totalPrice;

    @SerializedName("updateAt")
    private long updateAt;

    public CartEntity(String userId, int totalItems, int totalPrice, long updatedAt) {
        this.userId = userId;
        this.totalItems = totalItems;
        this.totalPrice = totalPrice;
        this.updateAt = updatedAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public long getUpdatedAt() {
        return updateAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updateAt = updatedAt;
    }
}
