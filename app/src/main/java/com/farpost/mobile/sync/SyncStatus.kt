package com.farpost.mobile.sync

/** Matches TapLog's ceiling — no failed-vs-queued distinction beyond this. */
sealed interface SyncStatus {
    data object Idle : SyncStatus
    data object Syncing : SyncStatus
    data object Partial : SyncStatus
    data object Error : SyncStatus
}
