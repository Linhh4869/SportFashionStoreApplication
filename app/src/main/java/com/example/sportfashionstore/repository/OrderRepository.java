package com.example.sportfashionstore.repository;

import com.google.firebase.firestore.FirebaseFirestore;

public class OrderRepository {
    private final FirebaseFirestore db;

    public OrderRepository() {
        this.db = FirebaseFirestore.getInstance();
    }
}
