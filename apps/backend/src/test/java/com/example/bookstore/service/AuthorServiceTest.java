package com.example.bookstore.service;

import com.example.bookstore.dto.AuthorRequest;
import com.example.bookstore.dto.AuthorResponse;
import com.example.bookstore.entity.Author;
import com.example.bookstore.exception.DuplicateEmailException;
import com.example.bookstore.exception.ResourceNotFoundException;
import com.example.bookstore.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    @Test
    void createAuthor_duplicateEmail_throwsConflict() {
        when(authorRepository.existsByEmail("dup@example.com")).thenReturn(true);

        AuthorRequest request = new AuthorRequest("A", "dup@example.com", null);

        assertThatThrownBy(() -> authorService.createAuthor(request))
                .isInstanceOf(DuplicateEmailException.class);
        verify(authorRepository, never()).save(any());
    }

    @Test
    void createAuthor_savesAndReturnsNumericId() {
        when(authorRepository.existsByEmail("a@example.com")).thenReturn(false);

        Author saved = new Author();
        saved.setId(1L);
        saved.setName("A");
        saved.setEmail("a@example.com");
        when(authorRepository.save(any(Author.class))).thenReturn(saved);

        AuthorResponse response = authorService.createAuthor(new AuthorRequest("A", "a@example.com", null));

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.email()).isEqualTo("a@example.com");
    }

    @Test
    void getAuthor_unknownId_throwsNotFound() {
        when(authorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authorService.getAuthor(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
