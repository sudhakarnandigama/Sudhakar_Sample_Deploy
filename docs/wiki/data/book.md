# Table: book

> **Sources** — interview Q6; README.md §9, §12
> **Status** — [spec]
> **Page-size budget** — used 84 / 300 lines

<a id="purpose"></a>
## Purpose

One row per catalog title, including its current stock level.

<a id="schema"></a>
## Schema

```sql
-- apps/backend/src/main/java/com/example/bookstore/entity/Book.java [planned]
CREATE TABLE book (
  id               INTEGER PRIMARY KEY,
  title            TEXT    NOT NULL,
  isbn             TEXT    NOT NULL,
  genre            TEXT    NOT NULL,
  publication_year INTEGER,
  price            NUMERIC,
  stock_level      INTEGER NOT NULL DEFAULT 0
);
CREATE UNIQUE INDEX ux_book_isbn ON book(isbn);
CREATE INDEX idx_book_title ON book(title);
```

<a id="columns"></a>
## Columns

| Column | Type | Nullable | Default | Constraint | Notes |
|---|---|---|---|---|---|
| id | BIGINT | no | auto | PK IDENTITY | |
| title | TEXT | no | — | — | |
| isbn | TEXT | no | — | UNIQUE | ISBN-13, 13 digits |
| genre | TEXT | no | — | — | enum as string (Genre) |
| publication_year | INTEGER | yes | NULL | — | |
| price | NUMERIC | yes | NULL | — | BigDecimal, non-negative |
| stock_level | INTEGER | no | 0 | ≥ 0 | enforced in StockService |

<a id="invariants"></a>
## Invariants

- `stock_level` ≥ 0 — enforced in `apps/backend/src/main/java/com/example/bookstore/service/StockService.java` `[planned]`.
- `isbn` is unique — enforced by `ux_book_isbn` and pre-checked in BookService.

<a id="read-patterns"></a>
## Read patterns

- By id → [api/books/get-book.md](../api/books/get-book.md)
- List → [api/books/list-books.md](../api/books/list-books.md)
- Search → [api/books/search-books.md](../api/books/search-books.md)

<a id="write-patterns"></a>
## Write patterns

- Insert → [api/books/create-book.md](../api/books/create-book.md)
- Update → [api/books/update-book.md](../api/books/update-book.md)
- Delete → [api/books/delete-book.md](../api/books/delete-book.md)
- Stock read → [api/stock/get-stock.md](../api/stock/get-stock.md)
- Stock set → [api/stock/set-stock.md](../api/stock/set-stock.md)
- Stock adjust → [api/stock/adjust-stock.md](../api/stock/adjust-stock.md)

<a id="volume"></a>
## Volume estimates

- Year 1: ~10k rows. Growth: ~50/day. Retention: indefinite.

<a id="verify"></a>
## Verify

```bash
sqlite3 bookstore.db "PRAGMA table_info(book);"
```
Expected: 7 columns matching §[Columns](#columns).
