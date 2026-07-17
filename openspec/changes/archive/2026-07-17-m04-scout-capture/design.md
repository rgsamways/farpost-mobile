## Context

The scout role needed a first, real end-to-end capture path before deciding what
a fuller capture model (building association, photos, a walk-based session)
should look like — building the richer model first, without proving the
local-first + outbox mechanics on something simple, risked discovering a
foundational problem only after investing in a bigger feature.

## Goals / Non-Goals

**Goals:** prove capture → local write → outbox enqueue → (eventually) sync,
end to end, on the smallest real feature that exercises the whole path.

**Non-Goals:** building the actual richer scout-walk capture model — that's later,
separate work, informed by what this minimal version reveals.

## Decisions

**Ship a flat, building-agnostic note log first, deliberately leaving
`buildingId`/`photoUri` unwired even though the repository already supports
them.** This has a modest genuine design consideration: it would have been just
as easy to wire a building picker immediately, but doing so before the offline-sync
path was proven working risked building UI on top of a foundation that might need
to change. Deferring it was a deliberate sequencing choice, not an oversight.

## Risks / Trade-offs

- **[Trade-off] A flat note log doesn't validate the app's real target use case
  (a location/building-aware field visit) — only the underlying mechanics.**
  Accepted — that validation is exactly what the next, separate scout-walk work is
  for.
