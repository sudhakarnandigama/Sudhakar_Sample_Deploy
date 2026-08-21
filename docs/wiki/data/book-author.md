# Table: book_author (join)

> **Sources** — interview Q6; README.md §9; `../sources/decisions/2026-08-21-005-many-to-many.md`
> **Status** — [spec]
> **Page-size budget** — used 45 / 300 lines

<a id="purpose"></a>
## Purpose

Join table for the many-to-many book↔author association. Owning side is `book`.

<a id="schema"></a>
## Schema

```sql
-- apps/backend/src/main/java/com/example/bookstore/entity/Book.java [planned]
CREATE TABLE book_author (
  book_id   BIGINT NOT NULL,
  author_id BIGINT NOT NULL,
  PRIMARY KEY (book_id, author_id)
);
```

<a id="columns"></a>
## Columns

| Column | Type | Nullable | Default | Constraint | Notes |
|---|---|---|---|---|---|
| book_id | BIGINT | no | — | PK, FK → book.id | |
| author_id | BIGINT | no | — | PK, FK → author.id | |

<a id="invariants"></a>
## Invariants

- Composite PK prevents duplicate (book, author) pairs.
- Deleting an author removes only join rows, never books (no cascade on the owning side) — see `../sources/decisions/2026-08-21-005-many-to-many.md`.

<a id="verify"></a>
## Verify

```bash
sqlite3 bookstore.db "PRAGMA table_info(book_author);"
```
Expected: 2 columns (`book_id`, `author_id`).
