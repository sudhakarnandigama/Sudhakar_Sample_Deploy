# Environment variables

> **Sources** — interview Q4, Q6; README.md §5; issue #6; `../../sources/decisions/2026-08-21-008-api-key-auth.md`
> **Status** — [spec]
> **Page-size budget** — used 54 / 300 lines

<a id="catalog"></a>
## Catalog

| Variable | Service | Required | Default | Secret | Read at | Purpose |
|---|---|---|---|---|---|---|
| `SERVER_PORT` | api | no | 8080 | no | Spring Boot auto-config | HTTP listen port |
| `SPRING_DATASOURCE_URL` | api | no | `jdbc:sqlite:bookstore.db` | no | `apps/backend/src/main/resources/application.properties` `[planned]` | SQLite file location |
| `SPRING_DATASOURCE_DRIVER_CLASS_NAME` | api | no | `org.sqlite.JDBC` | no | `apps/backend/src/main/resources/application.properties` `[planned]` | JDBC driver |
| `SPRING_JPA_DATABASE_PLATFORM` | api | no | `org.hibernate.community.dialect.SQLiteDialect` | no | `apps/backend/src/main/resources/application.properties` `[planned]` | Hibernate dialect |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | api | no | `update` | no | `apps/backend/src/main/resources/application.properties` `[planned]` | schema generation |
| `SPRING_JPA_SHOW_SQL` | api | no | `false` | no | `apps/backend/src/main/resources/application.properties` `[planned]` | SQL logging |
| `BOOKSTORE_API_KEY` | api | yes (stock mutations) | *(empty — fail closed)* | yes | `apps/backend/src/main/resources/application.properties` | API key for stock mutation endpoints |

<a id="loading"></a>
## Loading

Spring Boot's standard `application.properties` → environment-variable binding loads every variable above at startup. `SPRING_*` variables override their `application.properties` counterparts.

<a id="verify"></a>
## Verify

```bash
grep -c "spring\|server" docs/wiki/ops/env-vars.md
```
Expected: a count ≥ 6 (one line per catalogued variable mentions its Spring key).
