# Table: author

> **Sources** — interview Q6; README.md §9
> **Status** — [spec]
> **Page-size budget** — used 66 / 300 lines

<a id="purpose"></a>
## Purpose

One row per author credited on at least one book.

<a id="schema"></a>
## Schema

```sql
-- apps/backend/src/main/java/com/example/bookstore/entity/Author.java [planned]
CREATE TABLE author (
  id        INTEGER PRIMARY KEY,
  name      TEXT NOT NULL,
  email     TEXT NOT NULL,
  biography TEXT
);
CREATE UNIQUE INDEX ux_author_email ON author(email);
```

<a id="columns"></a>
## Columns

| Column | Type | Nullable | Default | Constraint | Notes |
|---|---|---|---|---|---|
| id | BIGINT | no | auto | PK IDENTITY | |
| name | TEXT | no | — | — | display name |
| email | TEXT | no | — | UNIQUE | RFC 5322 |
| biography | TEXT | yes | NULL | — | optional |

<a id="invariants"></a>
## Invariants

- `email` is unique among all rows (unique index `ux_author_email`).

<a id="read-patterns"></a>
## Read patterns

- By id → [api/authors/get-author.md](../api/authors/get-author.md)
- List all → [api/authors/list-authors.md](../api/authors/list-authors.md)
- Books of an author → [api/authors/list-author-books.md](../api/authors/list-author-books.md)

<a id="write-patterns"></a>
## Write patterns

- Insert → [api/authors/create-author.md](../api/authors/create-author.md)
- Update → [api/authors/update-author.md](../api/authors/update-author.md)
- Delete → [api/authors/delete-author.md](../api/authors/delete-author.md)

<a id="volume"></a>
## Volume estimates

- Year 1: ~1k rows. Growth: ~10/day. Retention: indefinite.

<a id="verify"></a>
## Verify

```bash
sqlite3 bookstore.db "PRAGMA table_info(author);"
```
Expected: 4 columns matching §[Columns](#columns).
