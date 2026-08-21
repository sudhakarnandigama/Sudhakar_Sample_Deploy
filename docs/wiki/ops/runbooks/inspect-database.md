# Runbook: inspect the SQLite database

> **Sources** — interview Q6; README.md §9
> **Status** — [spec]
> **Page-size budget** — used 34 / 200 lines

<a id="when-to-use-this"></a>
## When to use this

To inspect or debug persisted data without the running API.

<a id="pre-checks"></a>
## Pre-checks

- [ ] `sqlite3` CLI installed — `sqlite3 --version` returns a version.
- [ ] `bookstore.db` exists in the working directory — `ls bookstore.db`.

<a id="steps"></a>
## Steps

1. **List tables** — `sqlite3 bookstore.db ".tables"` — expected: `author  book  book_author`.
2. **Count books** — `sqlite3 bookstore.db "SELECT COUNT(*) FROM book;"` — expected: an integer.
3. **Read columns** — `sqlite3 bookstore.db "PRAGMA table_info(book);"` — expected: 7 columns.

<a id="verify"></a>
## Verify recovery

```bash
sqlite3 bookstore.db ".tables"
```
Expected: `author  book  book_author`.

<a id="rollback"></a>
## Rollback

Inspection is read-only; no rollback needed. Do not run `DELETE`/`DROP` against the live file.
