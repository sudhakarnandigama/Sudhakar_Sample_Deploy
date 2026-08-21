# Test fixtures

> **Sources** — README.md §11
> **Status** — [spec]
> **Page-size budget** — used 48 / 200 lines

<a id="purpose"></a>
## Purpose

Canonical seed data used by feature tests. Keep these values stable so test assertions stay deterministic.

<a id="authors"></a>
## Authors

| id | name | email | biography |
|---|---|---|---|
| 1 | J. K. Rowling | jk.rowling@example.com | British author. |
| 2 | Andy Weir | andy.weir@example.com | American novelist. |

<a id="books"></a>
## Books

| id | title | isbn | genre | publicationYear | price | stockLevel | authorIds |
|---|---|---|---|---|---|---|---|
| 1 | Harry Potter and the Philosopher's Stone | 9780747532699 | FANTASY | 1997 | 19.99 | 12 | [1] |
| 2 | The Martian | 9780553418026 | SCIENCE_FICTION | 2011 | 14.99 | 5 | [2] |

<a id="seed-sql"></a>
## Seed SQL

```sql
-- docs/wiki/test/fixtures.md — canonical seed data [spec]
INSERT INTO author (id, name, email, biography) VALUES
  (1, 'J. K. Rowling', 'jk.rowling@example.com', 'British author.'),
  (2, 'Andy Weir',       'andy.weir@example.com',  'American novelist.');

INSERT INTO book (id, title, isbn, genre, publication_year, price, stock_level) VALUES
  (1, 'Harry Potter and the Philosopher''s Stone', '9780747532699', 'FANTASY', 1997, 19.99, 12),
  (2, 'The Martian', '9780553418026', 'SCIENCE_FICTION', 2011, 14.99, 5);

INSERT INTO book_author (book_id, author_id) VALUES (1, 1), (2, 2);
```

<a id="verify"></a>
## Verify

```bash
grep -c "INSERT INTO" docs/wiki/test/fixtures.md
```
Expected: `3` (one per table: author, book, book_author).
