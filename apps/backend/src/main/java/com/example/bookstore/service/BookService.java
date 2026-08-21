package com.example.bookstore.service;

import com.example.bookstore.dto.AuthorResponse;
import com.example.bookstore.dto.AuthorSummary;
import com.example.bookstore.dto.BookRequest;
import com.example.bookstore.dto.BookResponse;
import com.example.bookstore.dto.PageResponse;
import com.example.bookstore.entity.Author;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Genre;
import com.example.bookstore.exception.DuplicateIsbnException;
import com.example.bookstore.exception.InvalidRequestException;
import com.example.bookstore.exception.ResourceNotFoundException;
import com.example.bookstore.repository.AuthorRepository;
import com.example.bookstore.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class BookService {

    private static final int MAX_PAGE_SIZE = 100;

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Transactional(readOnly = true)
    public PageResponse<BookResponse> listBooks(int page, int size, String sort) {
        Pageable pageable = buildPageable(page, size, sort);
        Page<BookResponse> result = bookRepository.findAll(pageable).map(this::toResponse);
        return new PageResponse<>(
                result.getContent(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages());
    }

    @Transactional(readOnly = true)
    public BookResponse getBook(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Transactional
    public BookResponse createBook(BookRequest request) {
        validatePublicationYear(request.publicationYear());
        if (bookRepository.existsByIsbn(request.isbn())) {
            throw new DuplicateIsbnException("ISBN already exists: " + request.isbn());
        }
        Set<Author> authors = resolveAuthors(request.authorIds());
        Book book = new Book();
        applyRequest(book, request, authors);
        return toResponse(bookRepository.save(book));
    }

    @Transactional
    public BookResponse updateBook(Long id, BookRequest request) {
        validatePublicationYear(request.publicationYear());
        Book book = findOrThrow(id);
        if (bookRepository.existsByIsbnAndIdNot(request.isbn(), id)) {
            throw new DuplicateIsbnException("ISBN already exists: " + request.isbn());
        }
        Set<Author> authors = resolveAuthors(request.authorIds());
        applyRequest(book, request, authors);
        return toResponse(bookRepository.save(book));
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = findOrThrow(id);
        bookRepository.delete(book);
    }

    @Transactional(readOnly = true)
    public List<AuthorResponse> listBookAuthors(Long id) {
        Book book = findOrThrow(id);
        return book.getAuthors().stream()
                .sorted(Comparator.comparing(Author::getId))
                .map(a -> new AuthorResponse(a.getId(), a.getName(), a.getEmail(), a.getBiography()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BookResponse> search(String title, String author, Genre genre) {
        boolean noTitle = title == null || title.isBlank();
        boolean noAuthor = author == null || author.isBlank();
        if (noTitle && noAuthor && genre == null) {
            return List.of();
        }
        String titlePattern = noTitle ? null : "%" + title.trim().toLowerCase() + "%";
        String authorPattern = noAuthor ? null : "%" + author.trim().toLowerCase() + "%";
        return bookRepository.search(titlePattern, authorPattern, genre).stream()
                .map(this::toResponse)
                .toList();
    }

    private Book findOrThrow(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", id));
    }

    private Set<Author> resolveAuthors(List<Long> authorIds) {
        Set<Long> ids = new HashSet<>(authorIds);
        Set<Author> authors = new HashSet<>(authorRepository.findAllById(ids));
        if (authors.size() != ids.size()) {
            throw new ResourceNotFoundException("One or more authors were not found");
        }
        return authors;
    }

    private void validatePublicationYear(Integer year) {
        if (year != null && year > Year.now().getValue()) {
            throw new InvalidRequestException("publicationYear must not be in the future");
        }
    }

    private void applyRequest(Book book, BookRequest request, Set<Author> authors) {
        book.setTitle(request.title());
        book.setIsbn(request.isbn());
        book.setGenre(request.genre());
        book.setPublicationYear(request.publicationYear());
        book.setPrice(request.price());
        book.setStockLevel(request.stockLevel());
        book.setAuthors(authors);
    }

    private Pageable buildPageable(int page, int size, String sort) {
        int safeSize = Math.min(Math.max(size, 1), MAX_PAGE_SIZE);
        int safePage = Math.max(page, 0);
        String[] parts = sort == null || sort.isBlank() ? new String[]{"title", "asc"} : sort.split(",");
        String field = parts[0].trim();
        Sort.Direction direction = parts.length > 1 && "desc".equalsIgnoreCase(parts[1].trim())
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        return PageRequest.of(safePage, safeSize, Sort.by(direction, field));
    }

    private BookResponse toResponse(Book book) {
        List<AuthorSummary> authors = book.getAuthors().stream()
                .sorted(Comparator.comparing(Author::getId))
                .map(a -> new AuthorSummary(a.getId(), a.getName(), a.getEmail()))
                .toList();
        return new BookResponse(book.getId(), book.getTitle(), book.getIsbn(), book.getGenre(),
                book.getPublicationYear(), book.getPrice(), book.getStockLevel(), authors);
    }
}
