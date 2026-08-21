package com.example.bookstore.repository;

import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    boolean existsByIsbn(String isbn);

    boolean existsByIsbnAndIdNot(String isbn, Long id);

    /**
     * Search by title (partial, case-insensitive), author name (partial,
     * case-insensitive), and genre (exact enum). Patterns are pre-built
     * lowercase LIKE expressions (e.g. {@code %harry%}) so the query avoids
     * dialect-specific CONCAT handling on SQLite.
     */
    @Query("""
            select distinct b from Book b
            left join b.authors a
            where (:titlePattern is null or lower(b.title) like :titlePattern)
              and (:authorPattern is null or lower(a.name) like :authorPattern)
              and (:genre is null or b.genre = :genre)
            """)
    List<Book> search(@Param("titlePattern") String titlePattern,
                      @Param("authorPattern") String authorPattern,
                      @Param("genre") Genre genre);
}
