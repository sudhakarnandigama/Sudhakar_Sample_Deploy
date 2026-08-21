package com.example.bookstore.controller;

import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Genre;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.StockHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StockControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private StockHistoryRepository stockHistoryRepository;

    private Book book;

    @BeforeEach
    void setUp() {
        stockHistoryRepository.deleteAll();
        bookRepository.deleteAll();
        book = new Book();
        book.setTitle("Harry Potter and the Philosopher's Stone");
        book.setIsbn("9780747532699");
        book.setGenre(Genre.FANTASY);
        book.setStockLevel(12);
        book = bookRepository.save(book);
    }

    @Test
    void getStock_returnsCurrentLevel() throws Exception {
        mockMvc.perform(get("/api/books/" + book.getId() + "/stock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId").value(book.getId()))
                .andExpect(jsonPath("$.stockLevel").value(12));
    }

    @Test
    void getStock_unknownBook_returns404() throws Exception {
        mockMvc.perform(get("/api/books/999/stock"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void setStock_returnsNewLevel() throws Exception {
        mockMvc.perform(put("/api/books/" + book.getId() + "/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"stockLevel\":25}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stockLevel").value(25));
    }

    @Test
    void setStock_negativeLevel_returns400() throws Exception {
        mockMvc.perform(put("/api/books/" + book.getId() + "/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"stockLevel\":-1}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void setStock_unknownBook_returns404() throws Exception {
        mockMvc.perform(put("/api/books/999/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"stockLevel\":5}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void adjustStock_negativeDelta_returnsReducedLevel() throws Exception {
        mockMvc.perform(patch("/api/books/" + book.getId() + "/stock/adjust")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"delta\":-5}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stockLevel").value(7));
    }

    @Test
    void adjustStock_positiveDelta_returnsIncreasedLevel() throws Exception {
        mockMvc.perform(patch("/api/books/" + book.getId() + "/stock/adjust")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"delta\":10}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stockLevel").value(22));
    }

    @Test
    void adjustStock_belowZero_returns409AndLeavesStockUnchanged() throws Exception {
        mockMvc.perform(patch("/api/books/" + book.getId() + "/stock/adjust")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"delta\":-100}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));

        mockMvc.perform(get("/api/books/" + book.getId() + "/stock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stockLevel").value(12));
    }

    @Test
    void adjustStock_unknownBook_returns404() throws Exception {
        mockMvc.perform(patch("/api/books/999/stock/adjust")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"delta\":-1}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void getStockHistory_newBook_returnsEmptyArray() throws Exception {
        mockMvc.perform(get("/api/books/" + book.getId() + "/stock/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void adjustStock_thenHistoryRecordsAdjustEntry() throws Exception {
        mockMvc.perform(patch("/api/books/" + book.getId() + "/stock/adjust")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"delta\":-5}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/books/" + book.getId() + "/stock/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookId").value(book.getId()))
                .andExpect(jsonPath("$[0].changeType").value("ADJUST"))
                .andExpect(jsonPath("$[0].previousLevel").value(12))
                .andExpect(jsonPath("$[0].newLevel").value(7));
    }

    @Test
    void setStock_thenHistoryRecordsSetEntry() throws Exception {
        mockMvc.perform(put("/api/books/" + book.getId() + "/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"stockLevel\":25}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/books/" + book.getId() + "/stock/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].changeType").value("SET"))
                .andExpect(jsonPath("$[0].previousLevel").value(12))
                .andExpect(jsonPath("$[0].newLevel").value(25));
    }

    @Test
    void getStockHistory_unknownBook_returns404() throws Exception {
        mockMvc.perform(get("/api/books/999/stock/history"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}
