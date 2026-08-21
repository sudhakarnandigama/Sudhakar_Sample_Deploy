# GET /api/books/{id} — get book

> **Sources** — interview Q4; [../../data/book.md](../../data/book.md)
> **Status** — [spec]
> **Page-size budget** — used 45 / 300 lines

<a id="purpose"></a>
## Purpose

Return one book by id.

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
```

### 404 Not Found — `NOT_FOUND`

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
| 1 | Existing id | 200 + book body |
| 2 | Unknown id | 404 |

<a id="verify"></a>
## Verify

```bash
curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/api/books/1
```
Expected: `200` (if id 1 exists) or `404`.
