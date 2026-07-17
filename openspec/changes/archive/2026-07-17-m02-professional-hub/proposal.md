## Why

Backfilling a real spec for work already shipped in the M01 skeleton commit
(`5f4ff49`, 2026-07-12) — every professional needs a landing screen after login
that shows what they can do, and one that extends cleanly as more roles get real
screens, rather than being rewritten per role.

## What Changes

- **New**: `ProfessionalHubScreen` renders one row per role the signed-in
  professional holds; a `"scout"` role's row is real and navigates to
  `ScoutCapture`, every other role renders as "Coming soon" until it has a real
  destination.
- No code changes. This proposal documents existing, shipped behavior.

## Capabilities

### New Capabilities
- `M02-professional-hub`: the role-row hub screen.

### Modified Capabilities
None.

## Impact

- **Docs only.** No files under `app/` are touched by this change.
