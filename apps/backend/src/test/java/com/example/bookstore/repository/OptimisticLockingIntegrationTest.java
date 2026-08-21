package com.example.bookstore.repository;

import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OptimisticLockingIntegrationTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    void versionIncrementsOnEachSequentialUpdate() {
        Book book = new Book();
        book.setTitle("Harry Potter and the Philosopher's Stone");
        book.setIsbn("9780747532699");
        book.setGenre(Genre.FANTASY);
        book.setStockLevel(5);
        Long id = bookRepository.saveAndFlush(book).getId();

        Book first = bookRepository.findById(id).orElseThrow();
        first.setStockLevel(10);
        int firstVersion = bookRepository.saveAndFlush(first).getVersion();

        Book second = bookRepository.findById(id).orElseThrow();
        second.setStockLevel(20);
        int secondVersion = bookRepository.saveAndFlush(second).getVersion();

        assertThat(firstVersion).isEqualTo(1);
        assertThat(secondVersion).isEqualTo(2);
    }
}
