package com.example.sportfashionstore.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cartEntities")
public class CartItemEntity {
    @PrimaryKey(autoGenerate = true)
    private long id;
    
    private String productId;

    private String variantId;

    private long finalPrice;

    private String name;

    private String description;

    private String color;

    private String size;

    private String imageUrl;

    private String addAt;

    public CartItemEntity(long id, String productId, String variantId, long finalPrice, String name, String description, String color, String size, String imageUrl, String addAt) {
        this.id = id;
        this.productId = productId;
        this.variantId = variantId;
        this.finalPrice = finalPrice;
        this.name = name;
        this.description = description;
        this.color = color;
        this.size = size;
        this.imageUrl = imageUrl;
        this.addAt = addAt;
    }



}
