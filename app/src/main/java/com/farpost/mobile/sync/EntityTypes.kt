package com.farpost.mobile.sync

/** [com.farpost.mobile.data.local.db.entity.PendingOperationEntity.entityType] values —
 * shared between whatever queues the outbox row (e.g. ScoutRepositoryImpl) and the
 * [EntrySyncHandler] registered to dispatch it (see SyncHandlerModule), so the two
 * sides can't drift apart on the string. */
object EntityTypes {
    const val SCOUT_ENTRY = "scout_entry"
}
