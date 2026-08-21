package com.example.bookstore.dto;

import com.example.bookstore.entity.Genre;

import java.math.BigDecimal;
import java.util.List;

public record BookResponse(
        Long id,
        String title,
        String isbn,
        Genre genre,
        Integer publicationYear,
        BigDecimal price,
        int stockLevel,
        List<AuthorSummary> authors) {
}
