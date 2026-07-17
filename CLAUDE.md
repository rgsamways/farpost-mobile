# Farpost Mobile — Claude Code Context

Read this file before writing any code, creating any file, or making any
architectural decision.

## What this is

The single Android app meant to eventually cover every Farpost role (adjuster,
contractor, broker, owner, scout, etc.) — Compose/Kotlin, package `com.farpost.mobile`.
It talks to the same backend as `farpost-web` (`farpost-api`, a sibling repo).

**`farpost-scout`** (`C:\dev\farpost-scout`) is a separate, older Android app
covering only the scout role. It is being retired once this app is trusted to
replace it — don't add new dependencies on it, and treat any pattern borrowed from
it as something to adapt, not a peer to keep in sync with.

Cross-repo pointers — these live in the **main Farpost repo**, `C:\dev\farpost`, not
here, because that's where the platform-wide docs already are:
- `docs/roles/scout.md` — current-state source of truth for the scout role
  specifically (backend + both mobile apps' real status, not aspirational).
- `docs/farpost-style-guide.md` — the cross-platform design-token source of truth
  (colors, type scale, shape, spacing) for `farpost-web`, `farpost-mobile`, and
  `farpost-scout` alike. Don't invent new tokens without checking this first.
- `docs/specs/farpost-mobile-*-handoff.md` — session handoffs for specific chunks of
  work on this app (e.g. offline-sync completion, hub skeleton). Written there
  rather than here so they sit alongside the platform's other planning docs.

## Stack

- Kotlin, Jetpack Compose, Material3
- Hilt (DI)
- Room (local persistence)
- Retrofit + OkHttp (network)
- WorkManager (background sync)
- DataStore (session/preferences persistence)
- Navigation-Compose, type-safe routes (`@Serializable object` per destination)

## Architecture rules (non-negotiable)

1. **Offline-first, always.** Every user-initiated write lands in Room first and
   returns immediately — never block a screen on a network call to save data.
   Syncing to the backend is a separate, later concern handled by the outbox
   pattern below, not something the write path waits on.
2. **The outbox is generic — don't give a new entity type its own sync table.**
   `PendingOperationEntity`/`PendingOperationDao` (`data/local/db/`) is keyed by
   `entityType`, holds a JSON `payload`, and is designed to hold any syncable write.
   A new feature that needs to sync registers a dispatch handler for its
   `entityType` in `SyncWorker`'s registry — it does not add a parallel outbox.
3. **Repositories own data access — ViewModels never touch Room, Retrofit, or
   DataStore directly.** Mirrors `farpost-api`'s equivalent rule. A ViewModel calls
   a repository interface; the repository is where Room DAOs / Retrofit services /
   DataStore live.
4. **One destination, one `@Serializable object`, registered in both
   `FarpostDestinations.kt` and `FarpostNavGraph.kt`.** No ad-hoc navigation, no
   destinations that exist in one file but not the other.
5. **The hub is the first screen after login, for every role, and its content is
   role-driven off real data — never hardcoded per-role branching that assumes a
   fixed role list.** `ProfessionalHubScreen` already does this correctly (iterates
   `professional.roles`) — follow that pattern for anything added to it.
6. **Capabilities are `ADMIN` / `PROFESSIONAL` / `OWNER` only.** This matches the
   backend's login-capability model exactly (confirmed 2026-07-17). Role-specific
   behavior (scout, adjuster, etc.) lives inside `ProfessionalProfile.roles`, never
   as its own capability — don't add one.
7. **Match `docs/farpost-style-guide.md`, don't invent parallel tokens.** Colors,
   type scale, spacing, and shape should come from `ui/theme/` values that already
   trace back to that guide. If something new is needed, add it to the guide too
   (it's meant to stay a real, current source of truth across all three
   Farpost-branded surfaces, not just describe `farpost-web`).

## Testing

No test suite exists yet as of 2026-07-17. When one is added, document the real
commands here — don't leave this section stale once tests exist.

## History

- **2026-07-12** — initial skeleton commit (`5f4ff49`): Compose UI shell, real
  Hilt/Room/WorkManager/DataStore wiring, role-scoped screens for admin/
  professional/owner/scout, a real (not fake) auth layer against farpost-api's live
  login endpoint, and a generic-but-not-yet-functional offline-sync outbox.
- **2026-07-17** — this file and `openspec/` added, so future sessions here have the
  same grounding `farpost-api`/`farpost-web` sessions already have. Two handoffs
  in flight: completing the offline-sync dispatch logic (`SyncWorker` currently
  only pings `/health`, never actually syncs), and building a two-column hub
  skeleton with a scout-walk entry point — see the handoff docs in the main repo's
  `docs/specs/` for full detail. Do the sync-completion one first.
