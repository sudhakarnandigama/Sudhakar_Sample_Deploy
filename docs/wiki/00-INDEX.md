# bookstore — wiki index

> **Sources** — interview, all wiki pages
> **Status** — [spec]
> **Page-size budget** — used 50 / 300 lines

This index is a routing table. Find your task on the left; click the link on the right. Do not load other pages "just in case" — that causes context bloat.

<a id="i-need-to"></a>
## I need to…

| Task | Page |
|---|---|
| Understand what the project does | [glossary.md](glossary.md#project) |
| See the full data model | [data/00-overview.md](data/00-overview.md) |
| Add/modify a database table | [data/author.md](data/author.md) · [data/book.md](data/book.md) · [data/book-author.md](data/book-author.md) · [data/stock-history.md](data/stock-history.md) |
| Add an API endpoint | [api/00-overview.md](api/00-overview.md) |
| Implement an author endpoint | [api/authors/00-overview.md](api/authors/00-overview.md) |
| Implement a book endpoint | [api/books/00-overview.md](api/books/00-overview.md) |
| Implement stock logic | [api/stock/00-overview.md](api/stock/00-overview.md) |
| View stock history | [api/stock/get-stock-history.md](api/stock/get-stock-history.md) |
| Check auth requirements | [auth/00-overview.md](auth/00-overview.md) |
| Add an env var | [ops/env-vars.md](ops/env-vars.md) |
| Run the app locally | [ops/runbooks/local-run.md](ops/runbooks/local-run.md) |
| Inspect the SQLite file | [ops/runbooks/inspect-database.md](ops/runbooks/inspect-database.md) |
| Run tests | [test/00-overview.md](test/00-overview.md) |
| Get canonical seed data | [test/fixtures.md](test/fixtures.md) |

<a id="glossary"></a>
## Glossary

[glossary.md](glossary.md) — every term used 3+ times in the wiki.

<a id="data"></a>
## Data model

[data/00-overview.md](data/00-overview.md) — tables: [author.md](data/author.md), [book.md](data/book.md), [book-author.md](data/book-author.md).

<a id="api"></a>
## API

[api/00-overview.md](api/00-overview.md) — resources: [authors](api/authors/00-overview.md), [books](api/books/00-overview.md), [stock](api/stock/00-overview.md).

<a id="ops"></a>
## Operations

[ops/00-overview.md](ops/00-overview.md) — [env-vars.md](ops/env-vars.md), runbooks: [local-run.md](ops/runbooks/local-run.md), [inspect-database.md](ops/runbooks/inspect-database.md).

<a id="long-pages"></a>
## Long pages (use Explore subagent to read)

*(none yet — split anything that hits 300 lines)*
