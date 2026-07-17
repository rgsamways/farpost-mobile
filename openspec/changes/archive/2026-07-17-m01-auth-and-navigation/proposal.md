## Why

Backfilling a real spec for work already shipped in the M01 skeleton commit
(`5f4ff49`, 2026-07-12) — every other screen in the app sits behind a real
sign-in, and nothing documented that flow as a requirement before now.

## What Changes

- **New**: real sign-in via `WelcomeScreen`, session persistence to DataStore,
  capability-based destination routing (`ADMIN`/`PROFESSIONAL`/`OWNER` priority),
  and sign-out from every signed-in home screen.
- No code changes. This proposal documents existing, shipped behavior.

## Capabilities

### New Capabilities
- `M01-auth-and-navigation`: sign-in, session persistence, capability-based
  routing, sign-out.

### Modified Capabilities
None.

## Impact

- **Docs only.** No files under `app/` are touched by this change.
