package com.example.bookstore.repository;

import com.example.bookstore.entity.Author;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void search_byTitleIsCaseInsensitiveAndPartial() {
        Author author = authorRepository.save(author("J. K. Rowling", "jk.rowling@example.com"));
        bookRepository.save(book("Harry Potter and the Philosopher's Stone", "9780747532699", Genre.FANTASY, author));

        List<Book> result = bookRepository.search("%harry%", null, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).contains("Harry Potter");
    }

    @Test
    void search_byAuthorNameIsPartial() {
        Author author = authorRepository.save(author("J. K. Rowling", "jk.rowling@example.com"));
        bookRepository.save(book("The Martian", "9780553418026", Genre.SCIENCE_FICTION, author));

        List<Book> result = bookRepository.search(null, "%rowling%", null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getIsbn()).isEqualTo("9780553418026");
    }

    @Test
    void search_byGenreIsExact() {
        Author author = authorRepository.save(author("Andy Weir", "andy.weir@example.com"));
        bookRepository.save(book("The Martian", "9780553418026", Genre.SCIENCE_FICTION, author));
        bookRepository.save(book("Other", "9780747532699", Genre.FANTASY, author));

        List<Book> result = bookRepository.search(null, null, Genre.FANTASY);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getGenre()).isEqualTo(Genre.FANTASY);
    }

    private Author author(String name, String email) {
        Author author = new Author();
        author.setName(name);
        author.setEmail(email);
        return author;
    }

    private Book book(String title, String isbn, Genre genre, Author author) {
        Book book = new Book();
        book.setTitle(title);
        book.setIsbn(isbn);
        book.setGenre(genre);
        book.setStockLevel(1);
        book.getAuthors().add(author);
        return book;
    }
}
