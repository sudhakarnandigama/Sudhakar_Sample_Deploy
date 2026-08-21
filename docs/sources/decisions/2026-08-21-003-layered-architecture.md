# ADR-003: Layered architecture (Controller → Service → Repository)

- **Date:** 2026-08-21
- **Status:** Accepted

## Context

Business rules (stock invariants, ISBN uniqueness) must be testable in isolation and reusable across entry points.

## Decision

Enforce a strict three-layer structure. Controllers translate HTTP to service calls and map DTOs; services own transactions and business rules; repositories own data access.

## Consequences

- Business rules are unit-testable without HTTP or a live database.
- Clean dependency direction: no upward imports.
- Additional mapping boilerplate between layers.
