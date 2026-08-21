package com.example.bookstore.controller;

import com.example.bookstore.entity.Author;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Genre;
import com.example.bookstore.repository.AuthorRepository;
import com.example.bookstore.repository.BookRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthorControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void cleanDatabase() {
        bookRepository.deleteAll();
        authorRepository.deleteAll();
    }

    @Test
    void listAuthors_emptyDatabase_returnsEmptyArray() throws Exception {
        mockMvc.perform(get("/api/authors"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void createAndGetAuthor_returns201Then200() throws Exception {
        MvcResult created = mockMvc.perform(post("/api/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"J. K. Rowling\",\"email\":\"jk.rowling@example.com\",\"biography\":\"British author.\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("J. K. Rowling"))
                .andExpect(jsonPath("$.email").value("jk.rowling@example.com"))
                .andReturn();

        long id = parseId(created);
        mockMvc.perform(get("/api/authors/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("J. K. Rowling"));
    }

    @Test
    void createAuthor_duplicateEmail_returns409() throws Exception {
        authorRepository.save(author("J. K. Rowling", "jk.rowling@example.com"));

        mockMvc.perform(post("/api/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Other\",\"email\":\"jk.rowling@example.com\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"));
    }

    @Test
    void createAuthor_malformedEmail_returns400() throws Exception {
        mockMvc.perform(post("/api/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"A\",\"email\":\"not-an-email\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void getAuthor_unknownId_returns404() throws Exception {
        mockMvc.perform(get("/api/authors/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void updateAuthor_returns200WithReplacedFields() throws Exception {
        Author saved = authorRepository.save(author("Old Name", "old@example.com"));

        mockMvc.perform(put("/api/authors/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"New Name\",\"email\":\"new@example.com\",\"biography\":\"bio\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.name").value("New Name"))
                .andExpect(jsonPath("$.email").value("new@example.com"));
    }

    @Test
    void deleteAuthor_returns204AndRemovesRow() throws Exception {
        Author saved = authorRepository.save(author("A", "a@example.com"));

        mockMvc.perform(delete("/api/authors/" + saved.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/authors/" + saved.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void listAuthorBooks_returnsBooksWithoutAuthors() throws Exception {
        Author saved = authorRepository.save(author("J. K. Rowling", "jk.rowling@example.com"));

        Book book = new Book();
        book.setTitle("Harry Potter and the Philosopher's Stone");
        book.setIsbn("9780747532699");
        book.setGenre(Genre.FANTASY);
        book.setPublicationYear(1997);
        book.setStockLevel(12);
        book.getAuthors().add(saved);
        bookRepository.save(book);

        mockMvc.perform(get("/api/authors/" + saved.getId() + "/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Harry Potter and the Philosopher's Stone"))
                .andExpect(jsonPath("$[0].stockLevel").value(12))
                .andExpect(jsonPath("$[0].authors").doesNotExist());
    }

    private Author author(String name, String email) {
        Author author = new Author();
        author.setName(name);
        author.setEmail(email);
        return author;
    }

    private long parseId(MvcResult result) throws Exception {
        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        return root.get("id").asLong();
    }
}
