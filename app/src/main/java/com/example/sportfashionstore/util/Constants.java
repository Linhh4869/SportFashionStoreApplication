package com.example.sportfashionstore.util;

public class Constants {
    public static class Role {
        public static final String BUYER = "BUYER";
        public static final String SELLER = "SELLER";
        public static final String SHIPPER = "SHIPPER";
    }

    public static class Collection {
        public static final String USERS = "users";
        public static final String SELLERS = "sellers";
        public static final String SHIPPERS = "shippers";
        public static final String CATEGORIES = "categories";
        public static final String PRODUCTS = "products";
        public static final String PRODUCT_VARIANTS = "productVariants";
        public static final String ORDERS = "orders";
        public static final String SHIPPER_ORDERS = "shipperOrders";
        public static final String NOTIFICATIONS = "notifications";
        public static final String REVIEWS = "reviews";
    }

    public enum Setting {
        CHANGE_ROLE,
        EDIT_INFORMATION,
        CHANGE_PASSWORD,
        SIGN_OUT,
        DELETE_ACCOUNT,
        LANGUAGE,
        THEME

    }
}
