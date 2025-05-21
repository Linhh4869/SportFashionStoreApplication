package com.example.sportfashionstore.util;

public class Constants {
    public static final String WEB_CLIENT_ID = "1013292885134-1af85bjde3k97tql2hah9ovik2l25pt9.apps.googleusercontent.com";
    public static final String PUBLIC_KEY = "cGtfdGVzdF81MVJQbTcxUnB6UHJOSUc1am1Xd25HcFQ1TlNwTzJweHBNRmwxSW5MeWJIMkF4ZFlJZFBEaGZuUDhSazR3b3ZreGJLMVdVRTZESENnMkdvcUV0M2RnZ0pVZTAwb3NmSlE1cW8=";
    public static final String SK_KEY = "c2tfdGVzdF81MVJQbTcxUnB6UHJOSUc1amxYMFhucHhveHpveVJuRk5SMVRsUktBdjJqUFloOE5XU1NZM2p6UTNJSFlqcjcxem9CRjhNOGhJWmhSUG9UcGM2V2RPeDlKTDAwZlZPTlZZZXg=";
    public static final String STRIPE_VERSION = "2025-04-30.basil";
    public static final String PAY_NOW = "pay_now";
    public static final String ADD_CART = "add_cart";

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
