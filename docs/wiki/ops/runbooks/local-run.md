# Runbook: run the app locally

> **Sources** — README.md §4
> **Status** — [spec]
> **Page-size budget** — used 36 / 200 lines

<a id="when-to-use-this"></a>
## When to use this

First boot of the API on a developer machine, or after a fresh clone.

<a id="pre-checks"></a>
## Pre-checks

- [ ] JDK 17 installed — `java -version` returns `17.x`.
- [ ] Maven installed — `mvn -version` returns `3.8+`.

<a id="steps"></a>
## Steps

1. **Build** — `mvn clean package` — expected: `BUILD SUCCESS`.
2. **Run** — `java -jar target/bookstore-0.0.1-SNAPSHOT.jar` — expected: `Started BookstoreApplication`.
3. **Smoke check** — `curl http://localhost:8080/api/books` — expected: HTTP 200.

<a id="verify"></a>
## Verify recovery

```bash
curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/api/books
```
Expected: `200`.

<a id="rollback"></a>
## Rollback if step 3 fails

`Ctrl+C` the process, delete `bookstore.db` (only if it is corrupt), and rerun from step 1.
