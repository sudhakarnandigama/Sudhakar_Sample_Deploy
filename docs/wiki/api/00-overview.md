# API overview

> **Sources** — interview Q4; README.md §10, §13
> **Status** — [spec]
> **Page-size budget** — used 64 / 200 lines

<a id="base-url"></a>
## Base URL

`http://localhost:8080/api`

<a id="headers"></a>
## Common headers

| Header | Required | Format |
|---|---|---|
| Content-Type | yes (when a body is sent) | `application/json` |
| Accept | no | `application/json` |

No authentication headers — every endpoint is public. See [../auth/00-overview.md](../auth/00-overview.md).

<a id="error-envelope"></a>
## Error envelope

Every non-2xx response has the same shape:

```json
{
  "timestamp": "2026-08-21T11:00:00Z",
  "status": 409,
  "error": "Conflict",
  "message": "ISBN already exists: 9780747532699",
  "path": "/api/books"
}
```

<a id="error-codes"></a>
## Error codes

| code | HTTP | Meaning |
|---|---|---|
| `INVALID_REQUEST` | 400 | Jakarta Bean Validation failed |
| `NOT_FOUND` | 404 | referenced id does not exist |
| `DUPLICATE_ISBN` | 409 | book `isbn` already exists |
| `DUPLICATE_EMAIL` | 409 | author `email` already exists |
| `INSUFFICIENT_STOCK` | 409 | stock would go below zero |
| `INTERNAL_ERROR` | 500 | unexpected exception |

<a id="id-format"></a>
## ID format

All ids are positive 64-bit integers (SQLite `INTEGER PRIMARY KEY`). Example: `1`, `42`.

<a id="pagination"></a>
## Pagination

`GET /books` accepts `page` (0-based, default `0`), `size` (default `20`), and `sort` (`field,dir`, default `title,asc`).

<a id="resources"></a>
## Resources

| Resource | Overview |
|---|---|
| authors | [authors/00-overview.md](authors/00-overview.md) |
| books | [books/00-overview.md](books/00-overview.md) |
| stock | [stock/00-overview.md](stock/00-overview.md) |

<a id="verify"></a>
## Verify

```bash
curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/api/books
```
Expected: `200`.
