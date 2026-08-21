# DELETE /api/authors/{id} — delete author

> **Sources** — interview Q4; [../../data/author.md](../../data/author.md); `../../../sources/decisions/2026-08-21-005-many-to-many.md`
> **Status** — [spec]
> **Page-size budget** — used 44 / 300 lines

<a id="purpose"></a>
## Purpose

Delete one author. Books linked to the author are NOT deleted; only `book_author` join rows are removed.

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

### 404 Not Found

`{ "status": 404, "error": "Not Found", "message": "Author 999 not found", "path": "/api/authors/999" }`

<a id="side-effects"></a>
## Side effects

1. Delete from `author` — see [../../data/author.md#write-patterns](../../data/author.md#write-patterns).
2. Delete join rows in `book_author` — see [../../data/book-author.md#invariants](../../data/book-author.md#invariants).
3. Books themselves remain — see ADR `../../../sources/decisions/2026-08-21-005-many-to-many.md`.

<a id="implementation"></a>
## Implementation

- Handler: `apps/backend/src/main/java/com/example/bookstore/controller/AuthorController.java` `[planned]`

<a id="test-plan"></a>
## Test plan

| # | Case | Expected |
|---|---|---|
| 1 | Existing id | 204; author gone, books remain |
| 2 | Unknown id | 404 |

<a id="verify"></a>
## Verify

```bash
curl -s -o /dev/null -w "%{http_code}" -X DELETE http://localhost:8080/api/authors/1
```
Expected: `204` (or `404` if id 1 is absent).
