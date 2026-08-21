# DELETE /api/books/{id} — delete book

> **Sources** — interview Q4; [../../data/book.md](../../data/book.md)
> **Status** — [spec]
> **Page-size budget** — used 40 / 300 lines

<a id="purpose"></a>
## Purpose

Delete one book. Join rows in `book_author` are removed with it; authors remain.

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

### 204 No Content

Empty body.

### 404 Not Found — `NOT_FOUND`

<a id="side-effects"></a>
## Side effects

1. Delete from `book` — see [../../data/book.md#write-patterns](../../data/book.md#write-patterns).
2. Delete join rows in `book_author` — see [../../data/book-author.md#columns](../../data/book-author.md#columns).

<a id="implementation"></a>
## Implementation

- Handler: `apps/backend/src/main/java/com/example/bookstore/controller/BookController.java` `[planned]`

<a id="test-plan"></a>
## Test plan

| # | Case | Expected |
|---|---|---|
| 1 | Existing id | 204; book and join rows gone |
| 2 | Unknown id | 404 |

<a id="verify"></a>
## Verify

```bash
curl -s -o /dev/null -w "%{http_code}" -X DELETE http://localhost:8080/api/books/1
```
Expected: `204` (or `404` if id 1 is absent).
