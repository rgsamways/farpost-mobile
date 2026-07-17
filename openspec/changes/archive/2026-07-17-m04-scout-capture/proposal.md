## Why

Backfilling a real spec for work already shipped in the M01 skeleton commit
(`5f4ff49`, 2026-07-12) — the scout role needed *some* way to capture field data
end-to-end (locally, offline-first) before committing to a richer capture model
(building association, photos, a walk-based session). A minimal flat note log
proves the local-first + outbox path works without over-building ahead of that
decision.

## What Changes

- **New**: `ScoutCaptureScreen` — a free-text note field, an entry list showing
  capture time and sync status, backed by local-first Room persistence via
  `ScoutRepository`.
- No code changes. This proposal documents existing, shipped behavior.

## Capabilities

### New Capabilities
- `M04-scout-capture`: local-first free-text field note capture for the scout
  role.

### Modified Capabilities
None.

## Impact

- **Docs only.** No files under `app/` are touched by this change.
