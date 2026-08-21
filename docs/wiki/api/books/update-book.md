# PUT /api/books/{id} — update book

> **Sources** — interview Q4; [../../data/book.md](../../data/book.md)
> **Status** — [spec]
> **Page-size budget** — used 58 / 300 lines

<a id="purpose"></a>
## Purpose

Replace all fields of one book, including its author links.

<a id="auth"></a>
## Auth

None — public endpoint. See [../../auth/00-overview.md](../../auth/00-overview.md).

<a id="request"></a>
## Request

| Param | Type | Required | Constraints |
|---|---|---|---|
| id | long | yes | positive integer |

### Body

| Field | Type | Required | Constraints | Default |
|---|---|---|---|---|
| title | string | yes | max 200 chars | — |
| isbn | string | yes | 13 digits | — |
| genre | string | yes | enum value | — |
| publicationYear | int | no | 1450..current year | null |
| price | number | no | ≥ 0 | null |
| stockLevel | int | yes | ≥ 0 | — |
| authorIds | long[] | yes | ≥ 1 element, each an existing author id | — |

<a id="responses"></a>
## Responses

### 200 OK

Updated book body (same shape as [get-book.md](get-book.md)).

### 400 Bad Request — `INVALID_REQUEST`

### 404 Not Found — `NOT_FOUND` (book id or an author id missing)

### 409 Conflict — `DUPLICATE_ISBN`

<a id="side-effects"></a>
## Side effects

1. Update `book` — see [../../data/book.md#write-patterns](../../data/book.md#write-patterns).
2. Replace `book_author` join rows — see [../../data/book-author.md#columns](../../data/book-author.md#columns).

<a id="implementation"></a>
## Implementation

- Handler: `apps/backend/src/main/java/com/example/bookstore/controller/BookController.java` `[planned]`

<a id="test-plan"></a>
## Test plan

| # | Case | Expected |
|---|---|---|
| 1 | Existing id + valid body | 200 + updated book |
| 2 | Unknown id | 404 |
| 3 | isbn collides with another book | 409 |

<a id="verify"></a>
## Verify

```bash
curl -i -X PUT http://localhost:8080/api/books/1 \
  -H "Content-Type: application/json" \
  -d '{"title":"Updated","isbn":"9780747532699","genre":"FICTION","stockLevel":3,"authorIds":[1]}'
```
Expected: HTTP 200 (or 404 if id 1 is absent).
