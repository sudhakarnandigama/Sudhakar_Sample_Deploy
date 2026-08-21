# GET /api/books — list books

> **Sources** — interview Q4; [../../data/book.md](../../data/book.md)
> **Status** — [spec]
> **Page-size budget** — used 50 / 300 lines

<a id="purpose"></a>
## Purpose

Return a paginated list of books.

<a id="auth"></a>
## Auth

None — public endpoint. See [../../auth/00-overview.md](../../auth/00-overview.md).

<a id="request"></a>
## Request

| Query | Type | Required | Default | Constraints |
|---|---|---|---|---|
| page | int | no | 0 | ≥ 0 |
| size | int | no | 20 | 1..100 |
| sort | string | no | title,asc | `field,dir` |

<a id="responses"></a>
## Responses

### 200 OK

```json
{
  "content": [
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
  ],
  "page": 0,
  "size": 20,
  "totalElements": 1,
  "totalPages": 1
}
```

<a id="side-effects"></a>
## Side effects

None.

<a id="implementation"></a>
## Implementation

- Handler: `apps/backend/src/main/java/com/example/bookstore/controller/BookController.java` `[planned]`
- Repository method: `BookRepository.findAll(Pageable)` `[planned]`

<a id="test-plan"></a>
## Test plan

| # | Case | Expected |
|---|---|---|
| 1 | Empty DB | 200 + `content: []` |
| 2 | size=1 with 2 books | 200 + 1 element, `totalPages: 2` |

<a id="verify"></a>
## Verify

```bash
curl -s "http://localhost:8080/api/books?page=0&size=5"
```
Expected: HTTP 200 with a `content` array and paging metadata.
