package com.example.sportfashionstore.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.PropertyName;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductVariant implements Serializable {
    @SerializedName("id")
    private String id;

    @SerializedName("productId")
    private String productId;

    @SerializedName("productVariantId")
    private String productVariantId;

    @SerializedName("size")
    private List<String> size;

    @SerializedName("price")
    private String price;

    @SerializedName("inventory")
    private String inventory;

    @SerializedName("image")
    private String image;

    @SerializedName("status")
    private String status;

    @PropertyName("desc")
    @SerializedName("desc")
    private String desc;

    @SerializedName("createdAt")
    private Timestamp createdAt;

    @SerializedName("updateAt")
    private Timestamp updateAt;

    public String getId() {
        return id != null ? id : "";
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public List<String> getSize() {
        return size != null ? size : new ArrayList<>();
    }

    public void setSize(List<String> size) {
        this.size = size;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getInventory() {
        return inventory != null ? inventory : "0";
    }

    public String getInvText() {
        if (inventory == null)
            return "";

        return "Kho: " + inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDesc() {
        return desc != null ? desc : "";
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getProductVariantId() {
        return productVariantId;
    }

    public void setProductVariantId(String productVariantId) {
        this.productVariantId = productVariantId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Timestamp updateAt) {
        this.updateAt = updateAt;
    }
}
