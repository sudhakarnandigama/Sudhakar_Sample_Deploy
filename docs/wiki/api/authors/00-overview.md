# Authors resource

> **Sources** — interview Q2, Q4; README.md §10.1
> **Status** — [spec]
> **Page-size budget** — used 32 / 150 lines

<a id="purpose"></a>
## Purpose

CRUD for the `author` table — see [../../data/author.md](../../data/author.md).

<a id="endpoints"></a>
## Endpoints

| Method | Path | Page |
|---|---|---|
| GET | `/api/authors` | [list-authors.md](list-authors.md) |
| GET | `/api/authors/{id}` | [get-author.md](get-author.md) |
| POST | `/api/authors` | [create-author.md](create-author.md) |
| PUT | `/api/authors/{id}` | [update-author.md](update-author.md) |
| DELETE | `/api/authors/{id}` | [delete-author.md](delete-author.md) |
| GET | `/api/authors/{id}/books` | [list-author-books.md](list-author-books.md) |

<a id="verify"></a>
## Verify

```bash
curl -s http://localhost:8080/api/authors
```
Expected: HTTP 200 JSON array.
