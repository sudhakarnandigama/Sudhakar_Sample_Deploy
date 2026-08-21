# ADR-002: Use Spring Data JPA (Hibernate) over raw JDBC

- **Date:** 2026-08-21
- **Status:** Accepted

## Context

The application needs type-safe entities and low boilerplate for CRUD operations.

## Decision

Use Spring Data JPA with Hibernate as the ORM, mapping entities to SQLite through the community `SQLiteDialect`.

## Consequences

- Repositories are declarative interfaces; query derivation covers most lookups.
- Schema auto-generation via `spring.jpa.hibernate.ddl-auto=update` speeds iteration.
- Must respect SQLite's limited type system (no native BOOLEAN/ENUM; see `docs/wiki/data/00-overview.md`).
