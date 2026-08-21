# PATCH /api/books/{id}/stock/adjust — adjust stock by delta

> **Sources** — interview Q4; [../../data/book.md#invariants](../../data/book.md#invariants); [00-overview.md#invariant](00-overview.md#invariant); issue #6; [../../auth/00-overview.md](../../auth/00-overview.md)
> **Status** — [spec]
> **Page-size budget** — used 60 / 300 lines

<a id="purpose"></a>
## Purpose

Add a signed delta to a book's stock level. Negative deltas represent sales or withdrawals.

<a id="auth"></a>
## Auth

Requires `X-API-Key` header — see [../../auth/00-overview.md](../../auth/00-overview.md).

<a id="request"></a>
## Request

| Param | Type | Required | Constraints |
|---|---|---|---|
| id | long | yes | positive integer |

### Body

| Field | Type | Required | Constraints | Default |
|---|---|---|---|---|
| delta | int | yes | any signed integer | — |

Schema source: `apps/backend/src/main/java/com/example/bookstore/dto/StockAdjustmentRequest.java` `[planned]`

<a id="responses"></a>
## Responses

### 200 OK

```json
{ "bookId": 1, "stockLevel": 7 }
```

### 400 Bad Request — `INVALID_REQUEST` (malformed body)

### 404 Not Found — `NOT_FOUND`

### 409 Conflict — `INSUFFICIENT_STOCK`

```json
{ "timestamp": "...", "status": 409, "error": "Conflict", "message": "Insufficient stock: cannot reduce stock level below zero.", "path": "/api/books/1/stock/adjust" }
```

<a id="side-effects"></a>
## Side effects

1. Update `book.stock_level` — see [../../data/book.md#write-patterns](../../data/book.md#write-patterns).
2. Reject the mutation when the result would be negative — invariant [../../data/book.md#invariants](../../data/book.md#invariants).

<a id="implementation"></a>
## Implementation

- Handler: `apps/backend/src/main/java/com/example/bookstore/controller/StockController.java` `[planned]`
- Service: `apps/backend/src/main/java/com/example/bookstore/service/StockService.java` `[planned]`

<a id="test-plan"></a>
## Test plan

| # | Case | Expected |
|---|---|---|
| 1 | delta = -5 from 12 | 200 + `stockLevel: 7` |
| 2 | delta = -100 from 12 | 409 `INSUFFICIENT_STOCK` |
| 3 | delta = +10 from 0 | 200 + `stockLevel: 10` |
| 4 | Unknown id | 404 |

<a id="verify"></a>
## Verify

```bash
curl -i -X PATCH http://localhost:8080/api/books/1/stock/adjust \
  -H "Content-Type: application/json" \
  -d '{"delta":-5}'
```
Expected: HTTP 200 with reduced `stockLevel`, or 409 `INSUFFICIENT_STOCK` if it would go below zero.
