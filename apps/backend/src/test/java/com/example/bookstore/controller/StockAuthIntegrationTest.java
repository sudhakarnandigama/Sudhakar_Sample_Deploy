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
class StockAuthIntegrationTest {

    private static final String API_KEY = "test-api-key";

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
    void setStock_withoutApiKey_returns401() throws Exception {
        mockMvc.perform(put("/api/books/" + book.getId() + "/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"stockLevel\":25}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401));
    }

    @Test
    void setStock_withWrongApiKey_returns401() throws Exception {
        mockMvc.perform(put("/api/books/" + book.getId() + "/stock")
                        .header("X-API-Key", "wrong-key")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"stockLevel\":25}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401));
    }

    @Test
    void setStock_withCorrectApiKey_returns200() throws Exception {
        mockMvc.perform(put("/api/books/" + book.getId() + "/stock")
                        .header("X-API-Key", API_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"stockLevel\":25}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stockLevel").value(25));
    }

    @Test
    void adjustStock_withoutApiKey_returns401() throws Exception {
        mockMvc.perform(patch("/api/books/" + book.getId() + "/stock/adjust")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"delta\":-1}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401));
    }

    @Test
    void adjustStock_withCorrectApiKey_returns200() throws Exception {
        mockMvc.perform(patch("/api/books/" + book.getId() + "/stock/adjust")
                        .header("X-API-Key", API_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"delta\":-1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stockLevel").value(11));
    }

    @Test
    void getStock_withoutApiKey_isPublic() throws Exception {
        mockMvc.perform(get("/api/books/" + book.getId() + "/stock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stockLevel").value(12));
    }

    @Test
    void listBooks_withoutApiKey_isPublic() throws Exception {
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void listAuthors_withoutApiKey_isPublic() throws Exception {
        mockMvc.perform(get("/api/authors"))
                .andExpect(status().isOk());
    }
}
