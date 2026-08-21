package com.example.bookstore.entity;

/**
 * Kind of stock mutation recorded in the audit trail.
 * Persisted as TEXT via {@code @Enumerated(EnumType.STRING)}.
 */
public enum StockChangeType {
    SET,
    ADJUST
}
