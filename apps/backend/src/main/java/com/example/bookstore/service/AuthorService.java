package com.example.bookstore.service;

import com.example.bookstore.dto.AuthorRequest;
import com.example.bookstore.dto.AuthorResponse;
import com.example.bookstore.dto.BookSummary;
import com.example.bookstore.entity.Author;
import com.example.bookstore.entity.Book;
import com.example.bookstore.exception.DuplicateEmailException;
import com.example.bookstore.exception.ResourceNotFoundException;
import com.example.bookstore.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Transactional(readOnly = true)
    public List<AuthorResponse> listAuthors() {
        return authorRepository.findAll().stream()
                .sorted(Comparator.comparing(Author::getId))
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public AuthorResponse getAuthor(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Transactional
    public AuthorResponse createAuthor(AuthorRequest request) {
        if (authorRepository.existsByEmail(request.email())) {
            throw new DuplicateEmailException("email already exists");
        }
        Author author = new Author();
        author.setName(request.name());
        author.setEmail(request.email());
        author.setBiography(request.biography());
        return toResponse(authorRepository.save(author));
    }

    @Transactional
    public AuthorResponse updateAuthor(Long id, AuthorRequest request) {
        Author author = findOrThrow(id);
        if (authorRepository.existsByEmailAndIdNot(request.email(), id)) {
            throw new DuplicateEmailException("email already exists");
        }
        author.setName(request.name());
        author.setEmail(request.email());
        author.setBiography(request.biography());
        return toResponse(authorRepository.save(author));
    }

    @Transactional
    public void deleteAuthor(Long id) {
        Author author = findOrThrow(id);
        authorRepository.delete(author);
    }

    @Transactional(readOnly = true)
    public List<BookSummary> listAuthorBooks(Long id) {
        Author author = findOrThrow(id);
        return author.getBooks().stream()
                .sorted(Comparator.comparing(Book::getId))
                .map(this::toBookSummary)
                .toList();
    }

    private Author findOrThrow(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author", id));
    }

    private AuthorResponse toResponse(Author author) {
        return new AuthorResponse(author.getId(), author.getName(), author.getEmail(), author.getBiography());
    }

    private BookSummary toBookSummary(Book book) {
        return new BookSummary(book.getId(), book.getTitle(), book.getIsbn(), book.getGenre(),
                book.getPublicationYear(), book.getPrice(), book.getStockLevel());
    }
}
