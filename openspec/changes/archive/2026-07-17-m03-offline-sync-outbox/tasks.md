## 1. Backfill

- [x] 1.1 Document the generic outbox schema and scheduling infrastructure as a spec — no code change, already shipped in the M01 skeleton commit.
- [x] 1.2 Explicitly record, as a requirement rather than a silent omission, that network delivery is not yet implemented — `SyncWorker` schedules and verifies connectivity but does not send.
