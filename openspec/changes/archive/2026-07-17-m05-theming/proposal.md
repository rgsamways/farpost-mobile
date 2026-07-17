## Why

Backfilling a real spec for work already shipped in the M01 skeleton commit
(`5f4ff49`, 2026-07-12) — `ui/theme/` was built to match `farpost-web`'s real,
live design tokens from day one, but nothing documented that alignment as a
requirement. Recorded now, alongside the other M0x backfills, so it isn't lost
before the app grows past a single commit.

## What Changes

- **New**: a spec documenting the app's existing color/shape/type-scale alignment
  with `docs/farpost-style-guide.md` (the cross-platform Farpost design-token
  source of truth), and the one known, tracked gap — a placeholder font instead
  of real Inter.
- No code changes. This proposal documents existing, shipped behavior.

## Capabilities

### New Capabilities
- `M05-theming`: color/shape/type-scale alignment with the cross-platform style
  guide.

### Modified Capabilities
None.

## Impact

- **Docs only.** No files under `app/` are touched by this change.
