# Table: stock_history

> **Sources** — issue #4; [../api/stock/get-stock-history.md](../api/stock/get-stock-history.md); `apps/backend/src/main/java/com/example/bookstore/entity/StockHistory.java`
> **Status** — [spec]
> **Page-size budget** — used 48 / 150 lines

<a id="purpose"></a>
## Purpose

Append-only audit trail of every stock mutation. One row per successful set or adjust.

<a id="schema"></a>
## Schema

```sql
-- apps/backend/src/main/java/com/example/bookstore/entity/StockHistory.java
CREATE TABLE stock_history (
  id             INTEGER PRIMARY KEY,
  book_id        BIGINT  NOT NULL,
  change_type    TEXT    NOT NULL,
  previous_level INTEGER NOT NULL,
  new_level      INTEGER NOT NULL,
  changed_at     TEXT    NOT NULL
);
```

<a id="columns"></a>
## Columns

| Column | Type | Nullable | Default | Constraint | Notes |
|---|---|---|---|---|---|
| id | BIGINT | no | auto | PK IDENTITY | |
| book_id | BIGINT | no | — | — | book id at change time; no FK so history survives book deletion |
| change_type | TEXT | no | — | — | enum as string: `SET`, `ADJUST` |
| previous_level | INTEGER | no | — | — | level before mutation |
| new_level | INTEGER | no | — | — | level after mutation |
| changed_at | TEXT | no | — | — | ISO-8601 UTC instant; lexical order == chronological order |

<a id="invariants"></a>
## Invariants

- Append-only: rows are inserted by stock mutations and never updated or deleted by them.
- Every successful stock set or stock adjust writes exactly one row.
- A rejected adjust (would go below zero) writes no row.

<a id="read-patterns"></a>
## Read patterns

- By book id, ordered by time → [../api/stock/get-stock-history.md](../api/stock/get-stock-history.md)

<a id="write-patterns"></a>
## Write patterns

- Insert only, from `StockService.setStock` and `StockService.adjustStock`.

<a id="verify"></a>
## Verify

```bash
sqlite3 bookstore.db "PRAGMA table_info(stock_history);"
```
Expected: 6 columns matching §[Columns](#columns).
