package com.example.sportfashionstore.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.sportfashionstore.util.Helper;
import com.google.firebase.Timestamp;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "carts")
public class CartEntity implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "product_id")
    private String productId;

    @ColumnInfo(name = "product_variant_id")
    private String productVariantId;

    private long price;

    @ColumnInfo(name = "sale_price")
    private long salePrice;

    private String size;

    @ColumnInfo(name = "is_sale_price")
    private boolean isSalePrice;

    private int quantity;

    private String description;

    private String image;

    private String color;

    private int isShowCart;

    @ColumnInfo(name = "created_at")
    private Timestamp createdAt;

    @ColumnInfo(name = "updated_at")
    private Timestamp updatedAt;

    public CartEntity(String productId, String productVariantId, long price, long salePrice, String size, boolean isSalePrice, int quantity, String description, String image, String color, Timestamp createdAt, Timestamp updatedAt, int isShowCart) {
        this.productId = productId;
        this.productVariantId = productVariantId;
        this.price = price;
        this.salePrice = salePrice;
        this.size = size;
        this.isSalePrice = isSalePrice;
        this.quantity = quantity;
        this.description = description;
        this.image = image;
        this.color = color;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isShowCart = isShowCart;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductVariantId() {
        return productVariantId;
    }

    public void setProductVariantId(String productVariantId) {
        this.productVariantId = productVariantId;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(long salePrice) {
        this.salePrice = salePrice;
    }

    public String getSize() {
        return size != null ? size : "";
    }

    public void setSize(String size) {
        this.size = size;
    }

    public boolean isSalePrice() {
        return isSalePrice;
    }

    public void setSalePrice(boolean salePrice) {
        isSalePrice = salePrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getColor() {
        return color != null ? color : "";
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int isShowCart() {
        return isShowCart;
    }

    public void setShowCart(int showCart) {
        isShowCart = showCart;
    }

    public String getParameter() {
        if (getColor().isEmpty() || getSize().isEmpty()) {
            return "";
        }
        return getColor() + ", " + getSize();
    }

    public String getDisplayQuantity() {
        return String.format("x%s", getQuantity());
    }

    public String getDisplayPrice() {
        int displayPrice = Math.toIntExact(getSalePrice() > 0 ? getSalePrice() : getPrice());
        return String.format("%sđ", Helper.formatPrice(displayPrice));
    }

    public String getDisplaySalePrice() {
        int displayPrice = (int) getPrice();
        return String.format("%sđ", Helper.formatPrice(displayPrice));
    }

    public CartEntity copy() {
        CartEntity copy = new CartEntity(
                this.productId,
                this.productVariantId,
                this.price,
                this.salePrice,
                this.size = size,
                this.isSalePrice,
                this.quantity,
                this.description,
                this.image,
                this.color,
                this.createdAt,
                this.updatedAt,
                this.isShowCart
        );
        copy.setId(this.id);
        copy.setCreatedAt(this.createdAt);
        copy.setUpdatedAt(Timestamp.now());
        return copy;
    }
}
