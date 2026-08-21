package com.example.bookstore.service;

import com.example.bookstore.dto.BookRequest;
import com.example.bookstore.entity.Author;
import com.example.bookstore.entity.Genre;
import com.example.bookstore.exception.DuplicateIsbnException;
import com.example.bookstore.exception.InvalidRequestException;
import com.example.bookstore.exception.ResourceNotFoundException;
import com.example.bookstore.repository.AuthorRepository;
import com.example.bookstore.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private BookService bookService;

    private BookRequest validRequest() {
        return new BookRequest("Title", "9780747532699", Genre.FICTION,
                2020, null, 5, List.of(1L));
    }

    @Test
    void createBook_duplicateIsbn_throwsConflict() {
        when(bookRepository.existsByIsbn("9780747532699")).thenReturn(true);

        assertThatThrownBy(() -> bookService.createBook(validRequest()))
                .isInstanceOf(DuplicateIsbnException.class);
        verify(bookRepository, never()).save(any());
    }

    @Test
    void createBook_unknownAuthor_throwsNotFound() {
        when(bookRepository.existsByIsbn("9780747532699")).thenReturn(false);
        when(authorRepository.findAllById(java.util.Set.of(1L))).thenReturn(List.of());

        assertThatThrownBy(() -> bookService.createBook(validRequest()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void createBook_futurePublicationYear_throwsInvalidRequest() {
        BookRequest request = new BookRequest("Title", "9780747532699", Genre.FICTION,
                2999, null, 5, List.of(1L));

        assertThatThrownBy(() -> bookService.createBook(request))
                .isInstanceOf(InvalidRequestException.class);
    }

    @Test
    void getBook_unknownId_throwsNotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.getBook(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void createBook_existingAuthors_savesBook() {
        when(bookRepository.existsByIsbn("9780747532699")).thenReturn(false);
        Author author = new Author();
        author.setId(1L);
        author.setName("J. K. Rowling");
        author.setEmail("jk.rowling@example.com");
        when(authorRepository.findAllById(java.util.Set.of(1L))).thenReturn(List.of(author));

        when(bookRepository.save(any(com.example.bookstore.entity.Book.class)))
                .thenAnswer(invocation -> {
                    com.example.bookstore.entity.Book book = invocation.getArgument(0);
                    book.setId(10L);
                    return book;
                });

        bookService.createBook(validRequest());

        verify(bookRepository).save(any());
    }
}
