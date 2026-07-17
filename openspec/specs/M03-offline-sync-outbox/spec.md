## Purpose

A generic, entity-agnostic pending-write queue plus the scheduling infrastructure
meant to eventually deliver every queued write to the backend. Backfilled
2026-07-17 from the M01 skeleton commit; no code changed to produce this spec.

**This spec documents a known, real gap, deliberately, rather than describing
aspirational behavior:** the queue and scheduling are real and working; the actual
network delivery is not. Completing it is separate, in-flight work — see
`docs/specs/farpost-mobile-offline-sync-completion-handoff.md` in the main Farpost
repo — and will modify the relevant requirement below once it lands, not before.

## Requirements

### Requirement: Generic outbox schema
The pending-write queue SHALL be keyed by an `entityType` string and SHALL store
`entityId`, an `operationType` (CREATE/UPDATE/DELETE), a JSON `payload`, an
`attemptCount`, and a last-attempt timestamp. A new syncable feature SHALL be able
to use this same table by registering under its own `entityType` — it SHALL NOT
require a new, parallel queue table.

#### Scenario: A new entity type reuses the existing schema
- **WHEN** a feature other than scout capture needs to queue an offline write
- **THEN** it can do so using the existing `PendingOperationEntity`/`PendingOperationDao` with no schema change

### Requirement: Local writes enqueue a pending operation
A scout-capture write SHALL insert its `ScoutEntryEntity` and its corresponding
`PendingOperationEntity` row in the same local transaction, so the two can never
diverge (an entry existing locally with no queued sync attempt, or vice versa).

#### Scenario: A capture always has a matching queued operation
- **WHEN** a scout note is captured
- **THEN** exactly one corresponding pending-operation row exists for it immediately afterward

### Requirement: Sync scheduling
The app SHALL schedule a periodic sync attempt every 15 minutes, constrained to
`NetworkType.CONNECTED`, unconditionally at startup. Every local write that
enqueues a pending operation SHALL also trigger an immediate one-time sync attempt,
under the same network constraint, with exponential backoff.

#### Scenario: A capture triggers an immediate sync attempt
- **WHEN** a scout note is captured
- **THEN** a one-time sync work request is enqueued immediately, in addition to the existing periodic schedule

### Requirement: Network delivery is not yet implemented
As of this spec, `SyncWorker` SHALL NOT deliver any pending operation to the
backend — it SHALL only verify connectivity (a `/health` check) when pending
operations exist. A pending operation SHALL therefore remain queued, and its
entity's `isSynced` flag SHALL remain `false`, indefinitely, regardless of
connectivity, until this requirement is superseded by the in-flight completion
work referenced in this spec's Purpose.

#### Scenario: A queued entry never becomes Synced under the current build
- **WHEN** a note is captured and the device later regains connectivity
- **THEN** the entry's status remains "Unsynced" — no automatic delivery occurs
