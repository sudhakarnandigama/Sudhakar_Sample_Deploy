# Roadmap

Wiki = current state. Roadmap = planned but not yet specced.

## Phase v1 (current)
- [x] Author CRUD — status: spec-ready
- [x] Book CRUD with many-to-many author linkage — status: spec-ready
- [x] Stock read / set / adjust endpoints — status: spec-ready
- [x] Search by title / author / genre — status: spec-ready

## Phase v2 (planned)
- [x] Stock history / audit log table — status: done (issue #4)
- [x] `@Version` optimistic locking for concurrent stock updates — status: done (issue #5)
- [x] Authentication (API key for stock mutations — see [ADR-008](sources/decisions/2026-08-21-008-api-key-auth.md)) — status: done (issue #6)
