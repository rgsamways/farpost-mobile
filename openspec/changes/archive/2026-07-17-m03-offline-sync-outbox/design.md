## Context

The predecessor app (TapLog) already had a mature offline-sync implementation:
every syncable Room entity carries its own `isSynced` column, and a single
`syncAll()` iterates each entity's table in a fixed order, calling a per-entity
network function for each unsynced row. That pattern works, but it means every
new syncable feature requires its own table-specific sync function and its own
`isSynced` column wired through by hand.

This app is meant to grow to cover every Farpost role over time — scout capture
today, other roles' field actions later — on a rural-connectivity user base where
"offline for extended periods, then briefly connected" is the normal case, not an
edge case.

## Goals / Non-Goals

**Goals:** a sync mechanism that (a) never blocks a local write on connectivity,
(b) generalizes to new capture features without per-feature sync plumbing, and
(c) actually delivers queued writes once connectivity returns, reliably enough
that a field user can trust their data made it.

**Non-Goals:** real conflict resolution beyond "the server already has this, stop
retrying" — building genuine field-level merge logic is a different, harder
problem this app doesn't need yet.

## Decisions

**A single generic outbox table (`entityType`/`entityId`/`operationType`/JSON
`payload`), instead of TapLog's per-entity `isSynced` + fixed-order `syncAll()`
pattern.** This was a genuine open question, not a foregone conclusion:
TapLog's approach is proven and simpler to reason about per-entity, at the cost of
every new syncable feature needing its own table wiring; a generic outbox is less
code per feature added later, at the cost of needing a real
dispatch-by-`entityType` mechanism (a registry, effectively) instead of leaning on
Kotlin's type system per table. The generic approach was chosen specifically
because this app is expected
to add many more syncable capture features across many roles over time — the
per-feature cost saved compounds, but the actual amount of that cost, and whether
the generic schema holds up cleanly once a second and third entity type actually
use it (not just scout capture), was not fully known when this shipped and still
isn't — that's honestly an open, not-yet-validated bet, tracked as ongoing risk
below, not a settled result.

**WorkManager's own `NetworkType.CONNECTED` constraint, not a custom
connectivity-change listener, for the initial scheduling mechanism.** Simpler to
implement, and correct in the common case, but a real, known trade-off in exactly
the environment this app is built for: WorkManager's constraint satisfaction is
coarser and can be slower to notice a network returning than reacting to the
platform's own connectivity callback the instant it fires. Whether that latency
difference actually matters to a field user (seconds vs. potentially longer) under
real rural network conditions was not measured before shipping this — it's a
genuine open question the completion work (in-flight, see below) is meant to
resolve empirically, not one this decision already answered.

## Risks / Trade-offs

- **[Risk] The generic outbox's `payload` is untyped JSON — a schema mismatch
  between what's queued and what a future dispatch handler expects only surfaces
  at dispatch/deserialization time, not at compile time.** Accepted for now;
  revisit if this causes real production bugs once a second entity type uses it.
- **[Risk] Network delivery is unimplemented as shipped.** `SyncWorker` currently
  proves scheduling/connectivity wiring (a `/health` check) but sends nothing —
  every queued write stays queued indefinitely under this build. This is real,
  tracked, and the subject of separate, immediately-following work — not treated
  as done here.
- **[Open question, not yet resolved] Does WorkManager's constraint-based
  scheduling deliver sync promptly enough in practice under genuinely poor rural
  connectivity, or does it need a real connectivity-listener trigger layered on
  top?** The completion work scavenges TapLog's connectivity-listener pattern
  specifically to test this, rather than assuming the answer either way.
