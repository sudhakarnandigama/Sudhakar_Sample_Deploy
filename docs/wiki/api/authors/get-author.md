# GET /api/authors/{id} — get author

> **Sources** — interview Q4; [../../data/author.md](../../data/author.md)
> **Status** — [spec]
> **Page-size budget** — used 44 / 300 lines

<a id="purpose"></a>
## Purpose

Return one author by id.

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
{
  "id": 1,
  "name": "J. K. Rowling",
  "email": "jk.rowling@example.com",
  "biography": "British author."
}
```

### 404 Not Found

```json
{ "timestamp": "...", "status": 404, "error": "Not Found", "message": "Author 999 not found", "path": "/api/authors/999" }
```

<a id="side-effects"></a>
## Side effects

None.

<a id="implementation"></a>
## Implementation

- Handler: `apps/backend/src/main/java/com/example/bookstore/controller/AuthorController.java` `[planned]`

<a id="test-plan"></a>
## Test plan

| # | Case | Expected |
|---|---|---|
| 1 | Existing id | 200 + author body |
| 2 | Unknown id | 404 `NOT_FOUND` |

<a id="verify"></a>
## Verify

```bash
curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/api/authors/1
```
Expected: `200` (if id 1 exists) or `404`.
