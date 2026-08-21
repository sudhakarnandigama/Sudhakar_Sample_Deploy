# Auth model

> **Sources** — interview Q5; issue #6; `../../sources/decisions/2026-08-21-008-api-key-auth.md`
> **Status** — [spec]
> **Page-size budget** — used 38 / 150 lines

<a id="current-state"></a>
## Current state

Stock mutation endpoints are protected with a static API key. Every other endpoint is public.

<a id="model"></a>
## Model

- Header: `X-API-Key`.
- Key value from env var `BOOKSTORE_API_KEY` (property `bookstore.api-key`) — see [../ops/env-vars.md](../ops/env-vars.md).
- Scope: the key grants one implicit `STOCK_MANAGER` scope covering `PUT /api/books/{id}/stock` and `PATCH /api/books/{id}/stock/adjust`.
- Missing or invalid key → 401 with the standard error envelope.
- Unset key → fail closed: all stock mutations return 401.

<a id="resolved"></a>
## Resolved

[GAP-AUTH-01] resolved by `../../sources/decisions/2026-08-21-008-api-key-auth.md` (issue #6).

<a id="verify"></a>
## Verify

```bash
curl -i -X PUT http://localhost:8080/api/books/1/stock \
  -H "Content-Type: application/json" -d '{"stockLevel":5}'
```
Expected without `X-API-Key`: HTTP 401.
