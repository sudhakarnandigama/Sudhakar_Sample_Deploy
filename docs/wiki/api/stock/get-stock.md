# GET /api/books/{id}/stock — get stock level

> **Sources** — interview Q4; [../../data/book.md#columns](../../data/book.md#columns)
> **Status** — [spec]
> **Page-size budget** — used 36 / 300 lines

<a id="purpose"></a>
## Purpose

Return the current stock level of one book.

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
{ "bookId": 1, "stockLevel": 12 }
```

### 404 Not Found — `NOT_FOUND`

<a id="side-effects"></a>
## Side effects

None.

<a id="implementation"></a>
## Implementation

- Handler: `apps/backend/src/main/java/com/example/bookstore/controller/StockController.java` `[planned]`

<a id="test-plan"></a>
## Test plan

| # | Case | Expected |
|---|---|---|
| 1 | Existing book | 200 + current level |
| 2 | Unknown id | 404 |

<a id="verify"></a>
## Verify

```bash
curl -s http://localhost:8080/api/books/1/stock
```
Expected: HTTP 200 with `bookId` and `stockLevel` (or 404 if id 1 is absent).
