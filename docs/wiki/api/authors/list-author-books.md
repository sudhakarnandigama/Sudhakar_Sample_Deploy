# GET /api/authors/{id}/books — list books by author

> **Sources** — interview Q4; [../../data/book-author.md](../../data/book-author.md)
> **Status** — [spec]
> **Page-size budget** — used 42 / 300 lines

<a id="purpose"></a>
## Purpose

Return every book linked to one author via `book_author`.

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
  {
    "id": 1,
    "title": "Harry Potter and the Philosopher's Stone",
    "isbn": "9780747532699",
    "genre": "FANTASY",
    "publicationYear": 1997,
    "price": 19.99,
    "stockLevel": 12
  }
]
```

### 404 Not Found — `NOT_FOUND` (author id does not exist)

<a id="side-effects"></a>
## Side effects

None.

<a id="implementation"></a>
## Implementation

- Handler: `apps/backend/src/main/java/com/example/bookstore/controller/AuthorController.java` `[planned]`

<a id="test-plan"></a>
## Test plan

| # | Case | Expected |
|---|---|---|
| 1 | Author with books | 200 + non-empty array |
| 2 | Author with no books | 200 + `[]` |
| 3 | Unknown author id | 404 |

<a id="verify"></a>
## Verify

```bash
curl -s http://localhost:8080/api/authors/1/books
```
Expected: HTTP 200 JSON array of book summaries.
