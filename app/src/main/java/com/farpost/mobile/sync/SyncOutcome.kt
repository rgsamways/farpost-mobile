package com.farpost.mobile.sync

/** Matches TapLog's ceiling — 200-or-409 is "done," anything else is a logged
 * failure that stays queued for retry. No richer conflict-resolution taxonomy. */
sealed interface SyncOutcome {
    data object Success : SyncOutcome
    data object Conflict : SyncOutcome
    data class Failure(val reason: String) : SyncOutcome
}
