package com.example.bookstore.controller;

import com.example.bookstore.dto.StockAdjustmentRequest;
import com.example.bookstore.dto.StockHistoryResponse;
import com.example.bookstore.dto.StockRequest;
import com.example.bookstore.dto.StockResponse;
import com.example.bookstore.service.StockService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/books/{bookId}/stock")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping
    public StockResponse getStock(@PathVariable Long bookId) {
        return stockService.getStock(bookId);
    }

    @PutMapping
    @SecurityRequirement(name = "ApiKeyAuth")
    public StockResponse setStock(@PathVariable Long bookId, @Valid @RequestBody StockRequest request) {
        return stockService.setStock(bookId, request.stockLevel());
    }

    @PatchMapping("/adjust")
    @SecurityRequirement(name = "ApiKeyAuth")
    public StockResponse adjustStock(@PathVariable Long bookId, @Valid @RequestBody StockAdjustmentRequest request) {
        return stockService.adjustStock(bookId, request.delta());
    }

    @GetMapping("/history")
    public List<StockHistoryResponse> getStockHistory(@PathVariable Long bookId) {
        return stockService.getStockHistory(bookId);
    }
}
