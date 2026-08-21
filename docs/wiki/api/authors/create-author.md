# POST /api/authors — create author

> **Sources** — interview Q4; [../../data/author.md](../../data/author.md)
> **Status** — [spec]
> **Page-size budget** — used 64 / 300 lines

<a id="purpose"></a>
## Purpose

Create one author row.

<a id="auth"></a>
## Auth

None — public endpoint. See [../../auth/00-overview.md](../../auth/00-overview.md).

<a id="request"></a>
## Request

### Headers

| Header | Required | Format |
|---|---|---|
| Content-Type | yes | `application/json` |

### Body

| Field | Type | Required | Constraints | Default |
|---|---|---|---|---|
| name | string | yes | max 100 chars | — |
| email | string | yes | RFC 5322, max 254 chars | — |
| biography | string | no | max 1000 chars | null |

Schema source: `apps/backend/src/main/java/com/example/bookstore/dto/AuthorRequest.java` `[planned]`

<a id="responses"></a>
## Responses

### 201 Created

```json
{ "id": 1, "name": "J. K. Rowling", "email": "jk.rowling@example.com", "biography": "British author." }
```

### 400 Bad Request

```json
{ "timestamp": "...", "status": 400, "error": "Bad Request", "message": "must be a well-formed email address", "path": "/api/authors" }
```

### 409 Conflict

```json
{ "timestamp": "...", "status": 409, "error": "Conflict", "message": "email already exists", "path": "/api/authors" }
```

<a id="side-effects"></a>
## Side effects

1. Insert into `author` — see [../../data/author.md#write-patterns](../../data/author.md#write-patterns).

<a id="implementation"></a>
## Implementation

- Handler: `apps/backend/src/main/java/com/example/bookstore/controller/AuthorController.java` `[planned]`
- Service: `apps/backend/src/main/java/com/example/bookstore/service/AuthorService.java` `[planned]`

<a id="test-plan"></a>
## Test plan

| # | Case | Expected |
|---|---|---|
| 1 | Valid body | 201 + body with numeric `id` |
| 2 | Duplicate email | 409 `DUPLICATE_EMAIL` |
| 3 | Invalid email | 400 `INVALID_REQUEST` |

<a id="verify"></a>
## Verify

```bash
curl -i -X POST http://localhost:8080/api/authors \
  -H "Content-Type: application/json" \
  -d '{"name":"J. K. Rowling","email":"jk.rowling@example.com"}'
```
Expected: HTTP 201, body has numeric `id`.
