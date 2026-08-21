package com.example.bookstore.entity;

/**
 * Book genres. Persisted as TEXT via {@code @Enumerated(EnumType.STRING)} because
 * SQLite has no native enum type; string storage keeps values human-readable and
 * stable if the constant order ever changes.
 */
public enum Genre {
    FICTION,
    NON_FICTION,
    SCIENCE_FICTION,
    FANTASY,
    MYSTERY,
    BIOGRAPHY
}
