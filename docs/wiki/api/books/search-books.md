# GET /api/books/search — search books

> **Sources** — interview Q4; [../../data/book.md](../../data/book.md)
> **Status** — [spec]
> **Page-size budget** — used 46 / 300 lines

<a id="purpose"></a>
## Purpose

Return books matching title, author name, or genre.

<a id="auth"></a>
## Auth

None — public endpoint. See [../../auth/00-overview.md](../../auth/00-overview.md).

<a id="request"></a>
## Request

| Query | Type | Required | Constraints |
|---|---|---|---|
| title | string | no | partial, case-insensitive substring |
| author | string | no | partial, case-insensitive substring on author name |
| genre | string | no | exact enum value |

At least one query parameter SHOULD be supplied.

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
    "stockLevel": 12,
    "authors": [ { "id": 1, "name": "J. K. Rowling", "email": "jk.rowling@example.com" } ]
  }
]
```

### 400 Bad Request — `INVALID_REQUEST` (e.g., unknown genre value)

<a id="side-effects"></a>
## Side effects

None.

<a id="implementation"></a>
## Implementation

- Handler: `apps/backend/src/main/java/com/example/bookstore/controller/BookController.java` `[planned]`
- Repository method: `BookRepository.search(...)` `[planned]`

<a id="test-plan"></a>
## Test plan

| # | Case | Expected |
|---|---|---|
| 1 | `?title=Harry` | 200 + matching books |
| 2 | `?author=Rowling` | 200 + books by that author |
| 3 | `?genre=FANTASY` | 200 + only FANTASY books |
| 4 | no params | 200 + `[]` or all books |

<a id="verify"></a>
## Verify

```bash
curl -s "http://localhost:8080/api/books/search?title=Harry"
```
Expected: HTTP 200 JSON array.
