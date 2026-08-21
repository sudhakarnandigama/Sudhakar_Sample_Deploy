# Bootstrap interview — 2026-08-21

Verbatim answers from Phase 1 of the Spec Bootstrap workflow. This file is the canonical Source for every wiki page that cites `interview Q<N>`. Append-only after creation.

## Q1 — PROJECT_NAME
bookstore

## Q2 — ONE_LINE_PURPOSE
bookstore is a REST API that manages book titles, authors, and stock levels.

## Q3 — ARCHITECTURE
single-app

## Q4 — PRIMARY_STACK
Spring Boot 3.2 + Java 17 + SQLite

## Q5 — AUTH_MODEL
none-yet

## Q6 — DATABASE / ORM
SQLite / Spring Data JPA (Hibernate)

## Q7 — DEPLOYABLE_UNITS
api

## Q8 — ROADMAP_PHASES
v1 (released); v2 (planned: stock history, optimistic locking)

## Q9 — EXTERNAL_READERS
no

## Q10 — SPEC_INPUT
README.md at repo root

## Scope contract (confirmed 2026-08-21)
1. Project: bookstore
2. Purpose: bookstore is a REST API that manages book titles, authors, and stock levels
3. Architecture: single-app (backend-only Spring Boot REST service)
4. Stack: Spring Boot 3.2 + Java 17 + SQLite
5. Auth: none-yet (README defines no authentication)
6. DB / ORM: SQLite / Spring Data JPA (Hibernate)
7. Deployables: api
8. Phases: v1 (released); v2 (planned: stock history, optimistic locking)
9. External readers: no
10. Spec input: README.md at repo root

## External URL allowlist
External URLs cited anywhere in the wiki must appear here. Add entries as the user supplies them; the audit script rejects any wiki link to a URL not in this list.

- (none declared)
