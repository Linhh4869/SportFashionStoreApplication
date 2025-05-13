package com.example.sportfashionstore.model;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class Order implements Serializable {
    private String orderId;
    private String userId;
    private String customerInfo;
    private String shipperId;
    private int status;
    private Timestamp createdAt;
    private Timestamp updateAt;
    private String userReasonCancel;
    private String shopReasonCancel;
    private String shipperReasonCancel;
    private String productId;
    private String variantId;
    private long price;
    private long salePrice;
    private boolean isSaleClothes;
    private String image;
    private String colorType;
    private String size;
    private String description;
    private int quantity;
    private String shipperInfo;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(String customerInfo) {
        this.customerInfo = customerInfo;
    }

    public String getShipperId() {
        return shipperId;
    }

    public void setShipperId(String shipperId) {
        this.shipperId = shipperId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getUserReasonCancel() {
        return userReasonCancel;
    }

    public void setUserReasonCancel(String userReasonCancel) {
        this.userReasonCancel = userReasonCancel;
    }

    public String getShopReasonCancel() {
        return shopReasonCancel;
    }

    public void setShopReasonCancel(String shopReasonCancel) {
        this.shopReasonCancel = shopReasonCancel;
    }

    public String getShipperReasonCancel() {
        return shipperReasonCancel;
    }

    public void setShipperReasonCancel(String shipperReasonCancel) {
        this.shipperReasonCancel = shipperReasonCancel;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getVariantId() {
        return variantId;
    }

    public void setVariantId(String variantId) {
        this.variantId = variantId;
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

    public boolean isSaleClothes() {
        return isSaleClothes;
    }

    public void setSaleClothes(boolean saleClothes) {
        isSaleClothes = saleClothes;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getColorType() {
        return colorType;
    }

    public void setColorType(String colorType) {
        this.colorType = colorType;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getShipperInfo() {
        return shipperInfo;
    }

    public void setShipperInfo(String shipperInfo) {
        this.shipperInfo = shipperInfo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
