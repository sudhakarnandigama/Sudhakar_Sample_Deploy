# GET /api/books/{id}/authors — list authors of a book

> **Sources** — interview Q4; [../../data/book-author.md](../../data/book-author.md)
> **Status** — [spec]
> **Page-size budget** — used 40 / 300 lines

<a id="purpose"></a>
## Purpose

Return every author linked to one book.

<a id="auth"></a>
## Auth

None — public endpoint. See [../../auth/00-overview.md](../../auth/00-overview.md).

<a id="request"></a>
## Request

| Param | Type | Required | Constraints |
|---|---|---|---|
| id | long | yes | positive integer |

<a id="responses"></a>
## Responses

### 200 OK

```json
[
  { "id": 1, "name": "J. K. Rowling", "email": "jk.rowling@example.com", "biography": "British author." }
]
```

### 404 Not Found — `NOT_FOUND` (book id does not exist)

<a id="side-effects"></a>
## Side effects

None.

<a id="implementation"></a>
## Implementation

- Handler: `apps/backend/src/main/java/com/example/bookstore/controller/BookController.java` `[planned]`

<a id="test-plan"></a>
## Test plan

| # | Case | Expected |
|---|---|---|
| 1 | Book with authors | 200 + non-empty array |
| 2 | Book with no authors | 200 + `[]` |
| 3 | Unknown book id | 404 |

<a id="verify"></a>
## Verify

```bash
curl -s http://localhost:8080/api/books/1/authors
```
Expected: HTTP 200 JSON array of authors.
