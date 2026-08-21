package com.example.bookstore.dto;

import com.example.bookstore.entity.Genre;

import java.math.BigDecimal;

/**
 * Compact book view without its author list, used by
 * GET /api/authors/{id}/books.
 */
public record BookSummary(
        Long id,
        String title,
        String isbn,
        Genre genre,
        Integer publicationYear,
        BigDecimal price,
        int stockLevel) {
}
