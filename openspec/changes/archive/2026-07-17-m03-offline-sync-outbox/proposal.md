## Why

Backfilling a real spec for work already shipped in the M01 skeleton commit
(`5f4ff49`, 2026-07-12) — this app is used in rural areas with genuinely
unreliable connectivity, so field-captured data must never be lost or blocked on
network availability, and must reach the backend once connectivity returns
without every new capture feature inventing its own bespoke sync mechanism.

## What Changes

- **New**: a generic, entity-agnostic pending-write outbox
  (`PendingOperationEntity`/`PendingOperationDao`) plus WorkManager-based
  scheduling (a 15-minute periodic sweep and an immediate trigger on every local
  write, both network-constrained with backoff).
- **Explicitly documents a known, real gap rather than aspirational behavior**:
  as shipped, the outbox correctly enqueues and schedules, but `SyncWorker` does
  not yet deliver any pending write to the backend. Completing that is separate,
  in-flight work.
- No code changes. This proposal documents existing, shipped behavior.

## Capabilities

### New Capabilities
- `M03-offline-sync-outbox`: the generic pending-write queue and its scheduling
  infrastructure.

### Modified Capabilities
None.

## Impact

- **Docs only.** No files under `app/` are touched by this change.
