package com.example.bookstore.exception;

import java.time.Instant;

/**
 * Uniform error envelope returned by every non-2xx response.
 * See docs/wiki/api/00-overview.md#error-envelope.
 */
public record ApiError(
        String timestamp,
        int status,
        String error,
        String message,
        String path) {

    public static ApiError of(int status, String error, String message, String path) {
        return new ApiError(Instant.now().toString(), status, error, message, path);
    }
}
