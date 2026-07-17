## Context

The backend already models a professional as holding an open-ended list of role
strings (`Professional.roles`), not a fixed enum — the client had to render
against that same open-ended shape rather than assuming a known, fixed role set.

## Goals / Non-Goals

**Goals:** a hub that never needs a code change just because a professional holds
a role the app doesn't have a screen for yet.

**Non-Goals:** building real screens for every role — only scout has one so far.

## Decisions

**Iterate `roles` and render "Coming soon" for anything without a real
destination, rather than filtering the list down to only roles with a real
screen.** This is a straightforward UI decision, not one with meaningful
technological uncertainty — a professional should see every role they hold, even
ones the app can't do anything with yet, so the list is an honest reflection of
their account rather than a curated subset.

## Risks / Trade-offs

- **[Trade-off] A single-column list doesn't scale well visually once several
  roles have real screens.** Accepted for now — a two-column layout is
  separate, in-flight work (see the hub-skeleton handoff), not solved here.
