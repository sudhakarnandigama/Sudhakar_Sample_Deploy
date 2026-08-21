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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Author rowling;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
        authorRepository.deleteAll();
        rowling = authorRepository.save(author("J. K. Rowling", "jk.rowling@example.com"));
    }

    @Test
    void createAndGetBook_returns201Then200WithAuthors() throws Exception {
        MvcResult created = mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson("9780747532699", "FANTASY", 12, rowling.getId())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").value("Harry Potter and the Philosopher's Stone"))
                .andExpect(jsonPath("$.authors[0].id").value(rowling.getId()))
                .andReturn();

        long id = parseId(created);
        mockMvc.perform(get("/api/books/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.stockLevel").value(12));
    }

    @Test
    void createBook_duplicateIsbn_returns409() throws Exception {
        bookRepository.save(existingBook("9780747532699"));

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson("9780747532699", "FICTION", 5, rowling.getId())))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void createBook_unknownAuthor_returns404() throws Exception {
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson("9780747532699", "FICTION", 5, 999L)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void createBook_invalidGenre_returns400() throws Exception {
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"T\",\"isbn\":\"9780747532699\",\"genre\":\"THRILLER\",\"stockLevel\":5,\"authorIds\":[" + rowling.getId() + "]}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void createBook_negativeStock_returns400() throws Exception {
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson("9780747532699", "FICTION", -1, rowling.getId())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void createBook_missingIsbn_returns400() throws Exception {
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"T\",\"genre\":\"FICTION\",\"stockLevel\":5,\"authorIds\":[" + rowling.getId() + "]}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void listBooks_returnsPagedEnvelope() throws Exception {
        bookRepository.save(existingBook("9780747532699"));

        mockMvc.perform(get("/api/books?page=0&size=20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(20))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.content[0].isbn").value("9780747532699"));
    }

    @Test
    void getBook_unknownId_returns404() throws Exception {
        mockMvc.perform(get("/api/books/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void updateBook_returns200WithReplacedFields() throws Exception {
        Book saved = bookRepository.save(existingBook("9780747532699"));

        mockMvc.perform(put("/api/books/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson("9780553418026", "SCIENCE_FICTION", 3, rowling.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value("9780553418026"))
                .andExpect(jsonPath("$.genre").value("SCIENCE_FICTION"))
                .andExpect(jsonPath("$.stockLevel").value(3));
    }

    @Test
    void deleteBook_returns204AndRemovesRow() throws Exception {
        Book saved = bookRepository.save(existingBook("9780747532699"));

        mockMvc.perform(delete("/api/books/" + saved.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/books/" + saved.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void listBookAuthors_returnsAuthors() throws Exception {
        Book saved = bookRepository.save(existingBook("9780747532699"));

        mockMvc.perform(get("/api/books/" + saved.getId() + "/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(rowling.getId()))
                .andExpect(jsonPath("$[0].name").value("J. K. Rowling"));
    }

    @Test
    void searchByTitle_returnsMatchingBooks() throws Exception {
        bookRepository.save(existingBook("9780747532699"));

        mockMvc.perform(get("/api/books/search").param("title", "harry"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Harry Potter and the Philosopher's Stone"));
    }

    @Test
    void searchByAuthor_returnsMatchingBooks() throws Exception {
        bookRepository.save(existingBook("9780747532699"));

        mockMvc.perform(get("/api/books/search").param("author", "rowling"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].authors[0].name").value("J. K. Rowling"));
    }

    @Test
    void searchByGenre_returnsOnlyMatchingGenre() throws Exception {
        bookRepository.save(existingBook("9780747532699"));

        mockMvc.perform(get("/api/books/search").param("genre", "FANTASY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].genre").value("FANTASY"));
    }

    @Test
    void searchByUnknownGenre_returns400() throws Exception {
        mockMvc.perform(get("/api/books/search").param("genre", "THRILLER"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    private Author author(String name, String email) {
        Author author = new Author();
        author.setName(name);
        author.setEmail(email);
        return author;
    }

    private Book existingBook(String isbn) {
        Book book = new Book();
        book.setTitle("Harry Potter and the Philosopher's Stone");
        book.setIsbn(isbn);
        book.setGenre(Genre.FANTASY);
        book.setPublicationYear(1997);
        book.setStockLevel(12);
        book.getAuthors().add(rowling);
        return book;
    }

    private String bookJson(String isbn, String genre, int stockLevel, long authorId) {
        return "{\"title\":\"Harry Potter and the Philosopher's Stone\","
                + "\"isbn\":\"" + isbn + "\","
                + "\"genre\":\"" + genre + "\","
                + "\"publicationYear\":1997,"
                + "\"price\":19.99,"
                + "\"stockLevel\":" + stockLevel + ","
                + "\"authorIds\":[" + authorId + "]}";
    }

    private long parseId(MvcResult result) throws Exception {
        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        return root.get("id").asLong();
    }
}
