package com.example.bookstore.controller;

import com.example.bookstore.dto.AuthorRequest;
import com.example.bookstore.dto.AuthorResponse;
import com.example.bookstore.dto.BookSummary;
import com.example.bookstore.service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public List<AuthorResponse> listAuthors() {
        return authorService.listAuthors();
    }

    @GetMapping("/{id}")
    public AuthorResponse getAuthor(@PathVariable Long id) {
        return authorService.getAuthor(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorResponse createAuthor(@Valid @RequestBody AuthorRequest request) {
        return authorService.createAuthor(request);
    }

    @PutMapping("/{id}")
    public AuthorResponse updateAuthor(@PathVariable Long id, @Valid @RequestBody AuthorRequest request) {
        return authorService.updateAuthor(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
    }

    @GetMapping("/{id}/books")
    public List<BookSummary> listAuthorBooks(@PathVariable Long id) {
        return authorService.listAuthorBooks(id);
    }
}
