# PUT /api/books/{id}/stock — set stock level

> **Sources** — interview Q4; [../../data/book.md#invariants](../../data/book.md#invariants)
> **Status** — [spec]
> **Page-size budget** — used 46 / 300 lines

<a id="purpose"></a>
## Purpose

Set the absolute stock level of one book.

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
| stockLevel | int | yes | ≥ 0 | — |

Schema source: `apps/backend/src/main/java/com/example/bookstore/dto/StockRequest.java` `[planned]`

<a id="responses"></a>
## Responses

### 200 OK

```json
{ "bookId": 1, "stockLevel": 25 }
```

### 400 Bad Request — `INVALID_REQUEST` (negative `stockLevel`)

### 404 Not Found — `NOT_FOUND`

<a id="side-effects"></a>
## Side effects

1. Update `book.stock_level` — see [../../data/book.md#write-patterns](../../data/book.md#write-patterns).

<a id="implementation"></a>
## Implementation

- Handler: `apps/backend/src/main/java/com/example/bookstore/controller/StockController.java` `[planned]`
- Service: `apps/backend/src/main/java/com/example/bookstore/service/StockService.java` `[planned]`

<a id="test-plan"></a>
## Test plan

| # | Case | Expected |
|---|---|---|
| 1 | stockLevel = 25 | 200 + `stockLevel: 25` |
| 2 | stockLevel = -1 | 400 |
| 3 | Unknown id | 404 |

<a id="verify"></a>
## Verify

```bash
curl -i -X PUT http://localhost:8080/api/books/1/stock \
  -H "Content-Type: application/json" \
  -d '{"stockLevel":25}'
```
Expected: HTTP 200 (or 404 if id 1 is absent).
