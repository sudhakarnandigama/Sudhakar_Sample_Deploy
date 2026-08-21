package com.example.bookstore.dto;

import java.util.List;

/**
 * Paginated envelope: content, page, size, totalElements, totalPages.
 * See docs/wiki/api/books/list-books.md.
 */
public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages) {
}
