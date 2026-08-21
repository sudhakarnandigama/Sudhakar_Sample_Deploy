# GET /api/authors — list authors

> **Sources** — interview Q4; [../../data/author.md](../../data/author.md)
> **Status** — [spec]
> **Page-size budget** — used 42 / 300 lines

<a id="purpose"></a>
## Purpose

Return every author.

<a id="auth"></a>
## Auth

None — public endpoint. See [../../auth/00-overview.md](../../auth/00-overview.md).

<a id="request"></a>
## Request

No body. No path or query parameters.

<a id="responses"></a>
## Responses

### 200 OK

```json
[
  {
    "id": 1,
    "name": "J. K. Rowling",
    "email": "jk.rowling@example.com",
    "biography": "British author."
  }
]
```

<a id="side-effects"></a>
## Side effects

None.

<a id="implementation"></a>
## Implementation

- Handler: `apps/backend/src/main/java/com/example/bookstore/controller/AuthorController.java` `[planned]`
- Repository method: `AuthorRepository.findAll()` `[planned]`

<a id="test-plan"></a>
## Test plan

| # | Case | Expected |
|---|---|---|
| 1 | Empty DB | 200 + `[]` |
| 2 | One author seeded | 200 + array of 1 |

<a id="verify"></a>
## Verify

```bash
curl -s http://localhost:8080/api/authors
```
Expected: HTTP 200 JSON array of author objects.
