# Operations overview

> **Sources** — interview Q3, Q4, Q7
> **Status** — [spec]
> **Page-size budget** — used 34 / 150 lines

<a id="environments"></a>
## Environments

| Environment | Deployable | Host | DB | Notes |
|---|---|---|---|---|
| dev (local) | `api` | `localhost:8080` | `bookstore.db` (working dir) | single JAR, SQLite file |

<a id="deploy-summary"></a>
## Deploy summary

One deployable unit (`api`) packaged as an executable JAR via Maven. No external database, queue, or cache. Deployment is copying the JAR plus its SQLite file to a host and running it.

<a id="related"></a>
## Related pages

- [env-vars.md](env-vars.md) — every configuration variable.
- [runbooks/local-run.md](runbooks/local-run.md) — run the app locally.
- [runbooks/inspect-database.md](runbooks/inspect-database.md) — inspect the SQLite file.

<a id="verify"></a>
## Verify

```bash
curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/api/books
```
Expected: `200` when the app is running.
