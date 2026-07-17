package com.farpost.mobile.sync

import com.farpost.mobile.data.local.db.entity.PendingOperationEntity

/** One implementation per [PendingOperationEntity.entityType], bound into a
 * Map<String, EntrySyncHandler> (see SyncHandlerModule) — SyncWorker dispatches by
 * lookup, not a when block that grows with every new syncable entity. Owns both the
 * network call and marking the local entity synced on success, since only the
 * handler knows which DAO/table that entityType maps to. */
interface EntrySyncHandler {
    suspend fun sync(operation: PendingOperationEntity): SyncOutcome
}
