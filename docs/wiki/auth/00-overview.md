# Auth model

> **Sources** — interview Q5
> **Status** — [spec]
> **Page-size budget** — used 26 / 150 lines

<a id="current-state"></a>
## Current state

No authentication. Every endpoint is public. `AUTH_MODEL = none-yet` (interview Q5).

<a id="gap"></a>
## Gap

[GAP-AUTH-01: authentication model not decided — no roles or scopes exist. Required before any protected endpoint is implemented.]

<a id="verify"></a>
## Verify

```bash
grep -rln "Authorization" docs/wiki/api/ || echo "no auth required anywhere"
```
Expected: prints `no auth required anywhere`.
