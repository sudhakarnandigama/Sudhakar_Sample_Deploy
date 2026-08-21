# ADR-001: Use SQLite as the database

- **Date:** 2026-08-21
- **Status:** Accepted

## Context

The system must be simple to run locally, portable, and free of external services. The data volume is modest (a book catalog with authors and stock levels).

## Decision

Use SQLite — an embedded, file-based SQL database. The database lives in a single file (`bookstore.db`) created automatically in the application working directory.

## Consequences

- Zero-installation deployment; no separate database server.
- Backups are file copies.
- Single-writer concurrency; unsuitable for high write-throughput multi-instance deployments.
- Requires the Hibernate community dialect (`org.hibernate.community.dialect.SQLiteDialect`) and the `org.xerial:sqlite-jdbc` driver, which are not in Spring Boot's default dependency set.
