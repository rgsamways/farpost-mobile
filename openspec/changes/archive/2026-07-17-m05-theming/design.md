## Context

`farpost-web`'s real, live design tokens (colors, type scale, shape/radius values)
were already documented in `docs/farpost-style-guide.md` before this app's first
commit — the guide predates and was written to serve `farpost-scout`, then
extended to cover this app and `farpost-web` itself as the source of truth.

## Goals / Non-Goals

**Goals:** `ui/theme/Color.kt`/`Theme.kt`/`Shape.kt` reproduce the guide's values
exactly, so the app doesn't invent a visually distinct design language from
`farpost-web`.

**Non-Goals:** dark mode (the guide explicitly states the real app has none),
pixel-perfect font rendering (Inter isn't bundled yet — a known, separate gap).

## Decisions

**Reproduce the guide's values directly, rather than deriving a separate Android
design system.** This is routine token-replication work, not a design decision
with real alternatives to weigh — there was one correct answer (match the
existing brand), not a choice between competing approaches. Recorded here for
completeness, not because genuine technological uncertainty existed.

## Risks / Trade-offs

- **[Risk] Font mismatch.** Using `FontFamily.Default` instead of Inter means the
  app doesn't yet look identical to `farpost-web`, even though colors/shapes
  match. Accepted for now — bundling real font files is separate, smaller work,
  tracked as a known gap rather than fixed here.
