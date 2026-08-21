# Spec Bootstrap workflow (archived 2026-08-21)

This is the Greenfield Documentation Bootstrap workflow that generated this wiki. Archived so future contributors know what produced the scaffold. The active operating rules live in `.nexgilerules` at the project root.

## Summary of the workflow

1. **Phase 1 — Interview.** Ten questions (project name, purpose, architecture, stack, auth, DB/ORM, deployables, phases, external readers, spec input) are answered and echoed back as a scope contract that must be confirmed before any file is written.
2. **Phase 2 — Scaffold.** Generate `docs/wiki/` with one starter file per page; `docs/sources/` for transcripts, decisions, research, external; `.nexgilerules` at project root.
3. **Phase 3 — Content.** Generate in strict bottom-up order: glossary → data model → auth model → domain events (skip if not event-driven) → API contracts → service/module map → UI flows (skip for backend-only) → integrations (skip if none) → operations → test strategy.
4. **Phase 4 — Self-check.** Nine audits: deeplink, page-size (≤300), orphan, forbidden-pattern, provenance, page-anatomy, operational-files, interview-citation, and (conditional) microservices structure. Any failure means fix and re-run all nine.
5. **Phase 5 — Operational files.** `.nexgilerules`, `docs/log.md`, `docs/roadmap.md`, and this archived workflow.

## Non-negotiable page properties

- **Concrete** — schemas as schemas, endpoints as endpoints, no vague verbs.
- **Linkable** — ≤300 lines, exact `page.md#anchor` or `path:line` cross-references.
- **Verifiable** — every page ends with a `## Verify` block containing one runnable command.

## Page anatomy (every wiki page)

1. Exactly one `# Title` (H1).
2. `> **Sources** — …` line.
3. `> **Status** — [spec] | [code] | [drift]` line.
4. `> **Page-size budget** — used N / cap lines` line.
5. `<a id="..."></a>` anchor immediately before each H2.
6. `<a id="verify"></a>` `## Verify` block with one runnable command.
7. ≤ page-size cap (300 most, 200 catalogs/glossary).

## Forbidden patterns (automatic rejection)

- "The system handles X" (vague verbs) → write the exact contract.
- "See the X docs for more" (broken link) → `page.md#anchor`.
- "TODO: decide later" without `[GAP-XX-NN]`.
- Page >300 lines → split.
- Same fact in two pages → one canonical page, everywhere else links.
- "Will support X in the future" → move to `docs/roadmap.md`.
- API section without endpoint, method, request schema, response schema, error codes.
- Field list without `(type, required, constraints, default)`.
- Code block without a file-path comment as its first line.
- Reference to a path that doesn't exist and isn't tagged `[planned]`.
- Sloppy GAP-ID → use `[GAP-XX-NN]` (2–8 letter category, 1–3 digit number).
- External URL not declared in the interview transcript's allowlist.
