# ADR-006: Centralize error handling with @RestControllerAdvice

- **Date:** 2026-08-21
- **Status:** Accepted

## Context

All endpoints must return a consistent JSON error envelope.

## Decision

Use a single global exception handler annotated `@RestControllerAdvice` that maps domain exceptions to a uniform error payload.

## Consequences

- Uniform error shape and HTTP status semantics across all endpoints.
- Controllers stay free of try/catch noise.
- The handler must be kept current as new exception types are introduced.
