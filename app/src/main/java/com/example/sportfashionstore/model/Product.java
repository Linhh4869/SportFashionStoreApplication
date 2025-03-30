package com.example.sportfashionstore.model;

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
        return sold != null ? "Da ban " + sold : "";
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
         Integer value = getSalePrice() > 0 ? getSalePrice() : getPrice();
         return String.valueOf(value);
    }

    public void setDisplayPrice(Integer displayPrice) {
        this.displayPrice = displayPrice;
    }
}
