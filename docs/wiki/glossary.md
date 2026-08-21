# Glossary

> **Sources** — interview Q1–Q10; README.md
> **Status** — [spec]
> **Page-size budget** — used 42 / 200 lines

<a id="project"></a>
**bookstore** — a backend-only Spring Boot 3.2 REST API (Java 17) that creates, reads, updates, and deletes book titles, authors, and stock levels. Stack: Spring Boot 3.2 + Java 17 + SQLite. Architecture: single-app. Routing table: [00-INDEX.md](00-INDEX.md).

<a id="author"></a>
**Author** — a person credited with writing one or more books. Stored in `author` table — see [data/author.md](data/author.md). Managed via [api/authors/00-overview.md](api/authors/00-overview.md).

<a id="book"></a>
**Book** — a catalog title with ISBN, genre, publication year, price, and stock level. Stored in `book` table — see [data/book.md](data/book.md). Managed via [api/books/00-overview.md](api/books/00-overview.md).

<a id="stock-level"></a>
**Stock level** — the non-negative integer count of copies on hand for a book. Column `stock_level` in [data/book.md](data/book.md). Invariant: never below zero — see [data/book.md#invariants](data/book.md#invariants).

<a id="genre"></a>
**Genre** — the classification of a book (`FICTION`, `NON_FICTION`, `SCIENCE_FICTION`, `FANTASY`, `MYSTERY`, `BIOGRAPHY`). Java enum persisted as TEXT — see [data/book.md](data/book.md).

<a id="isbn"></a>
**ISBN** — a unique catalog identifier for a book (e.g., `9780747532699`). Unique among all rows in `book` — see [data/book.md#invariants](data/book.md#invariants).

<a id="dto"></a>
**DTO** — data transfer object. The request/response shape exposed by the API, decoupled from JPA entities — see [../sources/decisions/2026-08-21-004-dto-boundary.md](../sources/decisions/2026-08-21-004-dto-boundary.md).

<a id="rest-resource"></a>
**REST resource** — a collection exposed by the API under `/api/<name>` (`authors`, `books`, `stock`). See [api/00-overview.md](api/00-overview.md).

<a id="verify"></a>
## Verify

```bash
wc -l < docs/wiki/glossary.md
```
Expected: fewer than 200 (glossary cap).
