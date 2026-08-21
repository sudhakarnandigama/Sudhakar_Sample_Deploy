# ADR-007: Use @Version optimistic locking for stock mutations

- **Date:** 2026-08-21
- **Status:** Accepted

## Context

Two requests can read the same book stock level and write back changes concurrently. With plain updates the second write silently overwrites the first (a lost update). Issue #5 requires detecting and rejecting such conflicts.

## Decision

Add a JPA `@Version` column to `Book`. Hibernate appends `version = version + 1` plus a `where version = ?` guard to every update, so a write based on a stale read updates zero rows and throws `OptimisticLockException`. The global exception handler maps that to HTTP 409 with retry guidance.

## Consequences

- Concurrent stock updates are detected and rejected instead of silently lost.
- Sequential (non-conflicting) updates continue to succeed.
- Error code: 409 Conflict with a message advising the client to reload and retry.
- Clients must handle 409 on stock mutations (set, adjust).
- The non-negative stock invariant is checked before any write, so it still holds under concurrency.
