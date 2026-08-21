# PUT /api/authors/{id} — update author

> **Sources** — interview Q4; [../../data/author.md](../../data/author.md)
> **Status** — [spec]
> **Page-size budget** — used 52 / 300 lines

<a id="purpose"></a>
## Purpose

Replace all fields of one author.

<a id="auth"></a>
## Auth

None — public endpoint. See [../../auth/00-overview.md](../../auth/00-overview.md).

<a id="request"></a>
## Request

### Path

| Param | Type | Required | Constraints |
|---|---|---|---|
| id | long | yes | positive integer |

### Body

| Field | Type | Required | Constraints | Default |
|---|---|---|---|---|
| name | string | yes | max 100 chars | — |
| email | string | yes | RFC 5322, max 254 chars | — |
| biography | string | no | max 1000 chars | null |

<a id="responses"></a>
## Responses

### 200 OK

```json
{ "id": 1, "name": "Updated Name", "email": "updated@example.com", "biography": null }
```

### 400 Bad Request — `INVALID_REQUEST`

### 404 Not Found — `NOT_FOUND`

### 409 Conflict — `DUPLICATE_EMAIL`

<a id="side-effects"></a>
## Side effects

1. Update row in `author` — see [../../data/author.md#write-patterns](../../data/author.md#write-patterns).

<a id="implementation"></a>
## Implementation

- Handler: `apps/backend/src/main/java/com/example/bookstore/controller/AuthorController.java` `[planned]`

<a id="test-plan"></a>
## Test plan

| # | Case | Expected |
|---|---|---|
| 1 | Existing id + valid body | 200 + updated body |
| 2 | Unknown id | 404 |
| 3 | Email collides with another author | 409 |

<a id="verify"></a>
## Verify

```bash
curl -i -X PUT http://localhost:8080/api/authors/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Updated","email":"updated@example.com"}'
```
Expected: HTTP 200 (or 404 if id 1 is absent).
