# Test strategy

> **Sources** — README.md §15
> **Status** — [spec]
> **Page-size budget** — used 38 / 200 lines

<a id="layers"></a>
## Layers

| Layer | What it tests | Tool | Command | Speed |
|---|---|---|---|---|
| unit | service business rules (stock guard, ISBN check), no IO | JUnit 5 + Mockito | `mvn test` | ms |
| web slice | controller mapping, validation, error statuses | MockMvc + `@WebMvcTest` | `mvn test` | ms |
| repository | query derivation + schema mapping | `@DataJpaTest` (SQLite) | `mvn test` | seconds |
| integration | full request → SQLite → response | `@SpringBootTest` + MockMvc | `mvn test` | tens of seconds |

<a id="fixtures"></a>
## Fixtures

See [fixtures.md](fixtures.md) — canonical seed data referenced from feature tests.

<a id="coverage-gates"></a>
## Coverage gates

- Unit: stock invariant and ISBN uniqueness covered (see [../data/book.md#invariants](../data/book.md#invariants)).
- Integration: every endpoint in [../api/](../api/00-overview.md) has ≥1 happy path and ≥1 error case.
- Repository: every custom query method in [../api/](../api/00-overview.md) is exercised.

<a id="verify"></a>
## Verify

```bash
mvn test
```
Expected: `BUILD SUCCESS` with 0 failures.
