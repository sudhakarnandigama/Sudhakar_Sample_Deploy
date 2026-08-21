package com.example.bookstore.dto;

public record StockResponse(
        Long bookId,
        int stockLevel) {
}
