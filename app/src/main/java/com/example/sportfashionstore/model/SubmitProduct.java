package com.example.sportfashionstore.model;

import com.google.firebase.Timestamp;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SubmitProduct implements Serializable {
    @SerializedName("id")
    private String id;

    @SerializedName("description")
    private String description;

    @SerializedName("categoryId")
    private String categoryId;

    @SerializedName("price")
    private Integer price;

    @SerializedName("salePrice")
    private Integer salePrice;

    @SerializedName("images")
    private List<String> images;

    @SerializedName("sold")
    private String sold;

    @SerializedName("rating")
    private String rating;

    @SerializedName("status")
    private String status;

    @SerializedName("saleClothes")
    private boolean saleClothes = false;

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

    public String getDescription() {
        return description != null ? description : "";
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getPrice() {
        return price != null ? price : 0;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getSalePrice() {
        return salePrice != null ? salePrice : 0;
    }

    public void setSalePrice(Integer salePrice) {
        this.salePrice = salePrice;
    }

    public List<String> getImages() {
        return images != null ? images : new ArrayList<>();
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getSold() {
        return sold != null ? sold : "";
    }

    public void setSold(String sold) {
        this.sold = sold;
    }

    public String getRating() {
        return rating != null ? rating : "";
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public boolean isSaleClothes() {
        return saleClothes;
    }

    public void setSaleClothes(boolean saleClothes) {
        this.saleClothes = saleClothes;
    }

    public String getStatus() {
        return status != null ? status : "";
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt != null ? createdAt : Timestamp.now();
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdateAt() {
        return updateAt != null ? updateAt : Timestamp.now();
    }

    public void setUpdateAt(Timestamp updateAt) {
        this.updateAt = updateAt;
    }
}
