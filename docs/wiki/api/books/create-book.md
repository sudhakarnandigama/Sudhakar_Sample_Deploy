# POST /api/books — create book

> **Sources** — interview Q4; [../../data/book.md](../../data/book.md); [../../data/book-author.md](../../data/book-author.md)
> **Status** — [spec]
> **Page-size budget** — used 84 / 300 lines

<a id="purpose"></a>
## Purpose

Create one book and link it to one or more authors.

<a id="auth"></a>
## Auth

None — public endpoint. See [../../auth/00-overview.md](../../auth/00-overview.md).

<a id="request"></a>
## Request

### Headers

| Header | Required | Format |
|---|---|---|
| Content-Type | yes | `application/json` |

### Body

| Field | Type | Required | Constraints | Default |
|---|---|---|---|---|
| title | string | yes | max 200 chars | — |
| isbn | string | yes | 13 digits | — |
| genre | string | yes | one of `FICTION`, `NON_FICTION`, `SCIENCE_FICTION`, `FANTASY`, `MYSTERY`, `BIOGRAPHY` | — |
| publicationYear | int | no | 1450..current year | null |
| price | number | no | ≥ 0 | null |
| stockLevel | int | yes | ≥ 0 | — |
| authorIds | long[] | yes | ≥ 1 element, each an existing author id | — |

Schema source: `apps/backend/src/main/java/com/example/bookstore/dto/BookRequest.java` `[planned]`

<a id="responses"></a>
## Responses

### 201 Created

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

### 400 Bad Request — `INVALID_REQUEST`

### 404 Not Found — `NOT_FOUND` (one or more `authorIds` do not exist)

### 409 Conflict — `DUPLICATE_ISBN`

<a id="side-effects"></a>
## Side effects

1. Insert into `book` — see [../../data/book.md#write-patterns](../../data/book.md#write-patterns).
2. Insert join rows into `book_author` — see [../../data/book-author.md#columns](../../data/book-author.md#columns).

<a id="implementation"></a>
## Implementation

- Handler: `apps/backend/src/main/java/com/example/bookstore/controller/BookController.java` `[planned]`
- Service: `apps/backend/src/main/java/com/example/bookstore/service/BookService.java` `[planned]`

<a id="test-plan"></a>
## Test plan

| # | Case | Expected |
|---|---|---|
| 1 | Valid body + existing authors | 201 + book with authors |
| 2 | Duplicate isbn | 409 `DUPLICATE_ISBN` |
| 3 | Unknown authorId | 404 `NOT_FOUND` |
| 4 | Invalid genre | 400 `INVALID_REQUEST` |
| 5 | Negative stockLevel | 400 `INVALID_REQUEST` |

<a id="verify"></a>
## Verify

```bash
curl -i -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{"title":"Sample","isbn":"9780747532699","genre":"FICTION","stockLevel":5,"authorIds":[1]}'
```
Expected: HTTP 201 (or 409 if the ISBN already exists).
