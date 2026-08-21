# Stock resource

> **Sources** — interview Q2, Q4; README.md §10.3; issue #4; issue #5; [get-stock-history.md](get-stock-history.md); `../../sources/decisions/2026-08-21-007-optimistic-locking.md`
> **Status** — [spec]
> **Page-size budget** — used 42 / 150 lines

<a id="purpose"></a>
## Purpose

Read and mutate `book.stock_level` — see [../../data/book.md#invariants](../../data/book.md#invariants).

<a id="invariant"></a>
## Invariant

Stock level must never go below zero. Enforced in `apps/backend/src/main/java/com/example/bookstore/service/StockService.java`.

<a id="concurrency"></a>
## Concurrency

`book` carries a `@Version` column. A stock mutation based on a stale read throws `OptimisticLockException`, which the global handler maps to 409 with retry guidance — see `../../sources/decisions/2026-08-21-007-optimistic-locking.md`. Clients must reload and retry on 409.

<a id="endpoints"></a>
## Endpoints

| Method | Path | Page |
|---|---|---|
| GET | `/api/books/{id}/stock` | [get-stock.md](get-stock.md) |
| PUT | `/api/books/{id}/stock` | [set-stock.md](set-stock.md) |
| PATCH | `/api/books/{id}/stock/adjust` | [adjust-stock.md](adjust-stock.md) |
| GET | `/api/books/{id}/stock/history` | [get-stock-history.md](get-stock-history.md) |

<a id="verify"></a>
## Verify

```bash
curl -s http://localhost:8080/api/books/1/stock
```
Expected: HTTP 200 with a `stockLevel` field (or 404 if id 1 is absent).
