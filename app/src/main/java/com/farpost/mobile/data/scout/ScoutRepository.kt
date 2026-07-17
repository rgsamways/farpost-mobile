package com.farpost.mobile.data.scout

import com.farpost.mobile.data.local.db.entity.ScoutEntryEntity
import kotlinx.coroutines.flow.Flow

interface ScoutRepository {
    fun observeEntries(): Flow<List<ScoutEntryEntity>>

    /** Local-first: writes to Room and queues an outbox operation in one transaction.
     * Returns immediately from the caller's perspective — sync happens later via
     * SyncWorker, never blocking this call on network. */
    suspend fun captureEntry(note: String, buildingId: String?, photoUri: String?)
}
