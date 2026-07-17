## Context

This app talks to the same login endpoint `farpost-web` already uses
(`POST /api/v1/auth/login`), so the session/capability model on the client had to
match what the backend actually returns, not an independently-invented shape.

## Goals / Non-Goals

**Goals:** a real session that survives app restart, and routing driven by real
capability data rather than a hardcoded per-user assumption.

**Non-Goals:** any new backend auth behavior — this is a client consuming an
existing endpoint.

## Decisions

**Decode the JWT's `sub` claim client-side, without verifying the signature.**
The client never needs to verify its own token's authenticity — it only needs the
user id to key local data, and re-sends the token itself on every request for the
server to verify. This is a standard, well-understood pattern, not a genuine
technological uncertainty — recorded here for completeness and to make the
security boundary explicit (verification happens server-side, always), not
because alternatives were seriously weighed.

**Capabilities are exactly `ADMIN`/`PROFESSIONAL`/`OWNER`, matching the backend
model 1:1.** No independent client-side capability model was invented — role-level
behavior (scout, etc.) lives inside `ProfessionalProfile.roles` instead, mirroring
how the backend itself treats it.

## Risks / Trade-offs

- **[Risk] An unverified client-side JWT decode is a bug magnet if ever reused for
  something security-sensitive.** Accepted — it's only ever used to key local
  DataStore/Room records, never to grant access to anything; the server remains
  the sole authority on the token's validity.
