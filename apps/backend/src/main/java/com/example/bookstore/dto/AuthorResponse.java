package com.example.bookstore.dto;

public record AuthorResponse(
        Long id,
        String name,
        String email,
        String biography) {
}
