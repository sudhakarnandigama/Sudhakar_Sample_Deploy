package com.example.bookstore.dto;

/**
 * Compact author view embedded in book responses (id, name, email only).
 */
public record AuthorSummary(
        Long id,
        String name,
        String email) {
}
