# GET /api/books/{id}/stock/history — list stock history

> **Sources** — issue #4; [../../data/stock-history.md](../../data/stock-history.md)
> **Status** — [spec]
> **Page-size budget** — used 50 / 300 lines

<a id="purpose"></a>
## Purpose

Return the append-only audit trail of stock changes for one book, ordered by time ascending (oldest first).

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
    "bookId": 1,
    "changeType": "SET",
    "previousLevel": 0,
    "newLevel": 12,
    "changedAt": "2026-08-21T12:00:00Z"
  }
]
```

### 404 Not Found — `NOT_FOUND` (book id does not exist)

<a id="side-effects"></a>
## Side effects

None.

<a id="implementation"></a>
## Implementation

- Handler: `apps/backend/src/main/java/com/example/bookstore/controller/StockController.java`
- Repository method: `StockHistoryRepository.findByBookIdOrderByChangedAtAscIdAsc`

<a id="test-plan"></a>
## Test plan

| # | Case | Expected |
|---|---|---|
| 1 | Book with no changes | 200 + `[]` |
| 2 | After one set/adjust | 200 + one entry with `changeType`, `previousLevel`, `newLevel` |
| 3 | Unknown book id | 404 `NOT_FOUND` |

<a id="verify"></a>
## Verify

```bash
curl -s http://localhost:8080/api/books/1/stock/history
```
Expected: HTTP 200 JSON array (or 404 if id 1 is absent).
