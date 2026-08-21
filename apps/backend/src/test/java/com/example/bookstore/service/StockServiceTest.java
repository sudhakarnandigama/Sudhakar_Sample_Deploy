package com.example.bookstore.service;

import com.example.bookstore.dto.StockHistoryResponse;
import com.example.bookstore.dto.StockResponse;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.StockChangeType;
import com.example.bookstore.entity.StockHistory;
import com.example.bookstore.exception.ResourceNotFoundException;
import com.example.bookstore.exception.StockConflictException;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.StockHistoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private StockHistoryRepository stockHistoryRepository;

    @InjectMocks
    private StockService stockService;

    private Book bookWithStock(int stock) {
        Book book = new Book();
        book.setId(1L);
        book.setStockLevel(stock);
        return book;
    }

    @Test
    void adjustStock_reducesStock() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookWithStock(12)));
        when(bookRepository.save(any(Book.class))).thenAnswer(inv -> inv.getArgument(0));

        StockResponse response = stockService.adjustStock(1L, -5);

        assertThat(response.stockLevel()).isEqualTo(7);
    }

    @Test
    void adjustStock_belowZero_throwsConflictAndDoesNotWrite() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookWithStock(12)));

        assertThatThrownBy(() -> stockService.adjustStock(1L, -100))
                .isInstanceOf(StockConflictException.class);
        verify(bookRepository, never()).save(any());
        verify(stockHistoryRepository, never()).save(any());
    }

    @Test
    void adjustStock_positiveDeltaFromZero_succeeds() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookWithStock(0)));
        when(bookRepository.save(any(Book.class))).thenAnswer(inv -> inv.getArgument(0));

        StockResponse response = stockService.adjustStock(1L, 10);

        assertThat(response.stockLevel()).isEqualTo(10);
    }

    @Test
    void adjustStock_recordsHistoryEntry() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookWithStock(12)));
        when(bookRepository.save(any(Book.class))).thenAnswer(inv -> inv.getArgument(0));

        stockService.adjustStock(1L, -5);

        ArgumentCaptor<StockHistory> captor = ArgumentCaptor.forClass(StockHistory.class);
        verify(stockHistoryRepository).save(captor.capture());
        StockHistory history = captor.getValue();
        assertThat(history.getBookId()).isEqualTo(1L);
        assertThat(history.getChangeType()).isEqualTo(StockChangeType.ADJUST);
        assertThat(history.getPreviousLevel()).isEqualTo(12);
        assertThat(history.getNewLevel()).isEqualTo(7);
    }

    @Test
    void setStock_recordsHistoryEntry() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookWithStock(12)));
        when(bookRepository.save(any(Book.class))).thenAnswer(inv -> inv.getArgument(0));

        stockService.setStock(1L, 25);

        ArgumentCaptor<StockHistory> captor = ArgumentCaptor.forClass(StockHistory.class);
        verify(stockHistoryRepository).save(captor.capture());
        StockHistory history = captor.getValue();
        assertThat(history.getChangeType()).isEqualTo(StockChangeType.SET);
        assertThat(history.getPreviousLevel()).isEqualTo(12);
        assertThat(history.getNewLevel()).isEqualTo(25);
    }

    @Test
    void setStock_unknownBook_throwsNotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> stockService.setStock(99L, 5))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getStockHistory_unknownBook_throwsNotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> stockService.getStockHistory(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getStockHistory_returnsEntries() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookWithStock(12)));
        StockHistory history = new StockHistory();
        history.setId(10L);
        history.setBookId(1L);
        history.setChangeType(StockChangeType.SET);
        history.setPreviousLevel(0);
        history.setNewLevel(12);
        history.setChangedAt("2026-08-21T12:00:00Z");
        when(stockHistoryRepository.findByBookIdOrderByChangedAtAscIdAsc(1L))
                .thenReturn(List.of(history));

        List<StockHistoryResponse> result = stockService.getStockHistory(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).newLevel()).isEqualTo(12);
    }
}
