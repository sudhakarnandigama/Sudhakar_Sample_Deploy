# Books resource

> **Sources** — interview Q2, Q4; README.md §10.2
> **Status** — [spec]
> **Page-size budget** — used 34 / 150 lines

<a id="purpose"></a>
## Purpose

CRUD and search for the `book` table — see [../../data/book.md](../../data/book.md).

<a id="endpoints"></a>
## Endpoints

| Method | Path | Page |
|---|---|---|
| GET | `/api/books` | [list-books.md](list-books.md) |
| GET | `/api/books/{id}` | [get-book.md](get-book.md) |
| POST | `/api/books` | [create-book.md](create-book.md) |
| PUT | `/api/books/{id}` | [update-book.md](update-book.md) |
| DELETE | `/api/books/{id}` | [delete-book.md](delete-book.md) |
| GET | `/api/books/{id}/authors` | [list-book-authors.md](list-book-authors.md) |
| GET | `/api/books/search` | [search-books.md](search-books.md) |

Stock endpoints for a book live under [../stock/00-overview.md](../stock/00-overview.md).

<a id="verify"></a>
## Verify

```bash
curl -s http://localhost:8080/api/books
```
Expected: HTTP 200 JSON array (paginated list).
