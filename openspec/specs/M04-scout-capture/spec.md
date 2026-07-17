## Purpose

A local-first, free-text field note log for the scout role. Backfilled 2026-07-17
from the M01 skeleton commit; no code changed to produce this spec. This is
deliberately narrower than a future "scout walk" — a flat running note log, not a
building-associated or session-based capture flow — and SHALL NOT be assumed
interchangeable with that later feature without a separate decision.

## Requirements

### Requirement: Local-first capture
Submitting a note SHALL write it to local Room storage and SHALL return
immediately, without waiting on network connectivity or a sync attempt to
complete.

#### Scenario: Capture succeeds while offline
- **WHEN** a scout submits a note with no network connection present
- **THEN** the note is saved locally and appears in the entry list immediately

### Requirement: Entries display with a sync status label
Each captured entry SHALL display its capture timestamp and a binary
"Synced"/"Unsynced" label reflecting its local `isSynced` flag.

#### Scenario: A freshly captured entry shows as Unsynced
- **WHEN** a note is captured
- **THEN** it appears in the list labeled "Unsynced" until its `isSynced` flag is set

### Requirement: Building and photo association are not yet implemented
The underlying repository SHALL support an optional `buildingId` and `photoUri` on
a captured entry, but the current screen SHALL always pass `null` for both — there
is no UI yet for associating a note with a specific building or attaching a photo.

#### Scenario: A captured entry has no building or photo association
- **WHEN** a note is captured through the current screen
- **THEN** the resulting entry's `buildingId` and `photoUri` are both `null`
