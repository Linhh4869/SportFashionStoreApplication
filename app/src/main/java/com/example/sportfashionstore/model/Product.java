package com.example.sportfashionstore.model;

import com.example.sportfashionstore.util.Helper;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Product implements Serializable {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

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

    @SerializedName("inventory")
    private String inventory;

    @SerializedName("sold")
    private String sold;

    @SerializedName("rating")
    private String rating;

    @SerializedName("status")
    private String status;

    @SerializedName("saleClothes")
    private boolean saleClothes;

    private Integer displayPrice;

    public String getId() {
        return id != null ? id : "";
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name != null ? name : "";
    }

    public void setName(String name) {
        this.name = name;
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

    public String getInventory() {
        return inventory != null ? inventory : "";
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    public String getSold() {
        return sold != null ? "Đã bán " + sold : "";
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

    public String getStatus() {
        return status != null ? status : "";
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDisplayPrice() {
         displayPrice = getSalePrice() > 0 ? getSalePrice() : getPrice();
         return String.format("%sđ", Helper.formatPrice(displayPrice));
    }

    public void setDisplayPrice(Integer displayPrice) {
        this.displayPrice = displayPrice;
    }

    public boolean isSaleClothes() {
        return saleClothes;
    }

    public void setSaleClothes(boolean saleClothes) {
        this.saleClothes = saleClothes;
    }
}
