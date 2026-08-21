package com.example.bookstore.dto;

import com.example.bookstore.entity.Genre;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

public record BookRequest(
        @NotBlank @Size(max = 200) String title,
        @NotBlank @Pattern(regexp = "^\\d{13}$") String isbn,
        @NotNull Genre genre,
        @Min(1450) Integer publicationYear,
        @DecimalMin("0.0") BigDecimal price,
        @NotNull @Min(0) Integer stockLevel,
        @NotEmpty List<Long> authorIds) {
}
