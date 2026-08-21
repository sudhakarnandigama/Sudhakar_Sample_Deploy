package com.example.bookstore.dto;

import com.example.bookstore.entity.StockChangeType;

public record StockHistoryResponse(
        Long id,
        Long bookId,
        StockChangeType changeType,
        int previousLevel,
        int newLevel,
        String changedAt) {
}
