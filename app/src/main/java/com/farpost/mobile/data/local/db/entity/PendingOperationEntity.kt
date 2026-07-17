package com.farpost.mobile.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * The offline write queue ("outbox pattern") — see farpost-mobile-tech-stack memory.
 * Every offline write inserts a row here in the same transaction as the local Room
 * write, and SyncWorker replays these in order once connectivity returns. [id]
 * doubles as the idempotency key sent to the backend, so a retried sync can't
 * create a duplicate record.
 */
@Entity(tableName = "pending_operations")
data class PendingOperationEntity(
    @PrimaryKey val id: String,
    /** e.g. "scout_entry" — which local table/repository this operation belongs to. */
    val entityType: String,
    /** Local id of the affected row (matches the id in that entity's own table). */
    val entityId: String,
    /** "CREATE" | "UPDATE" | "DELETE" — kept as a plain string to avoid a TypeConverter. */
    val operationType: String,
    /** JSON snapshot of the entity at the time it was queued. */
    val payload: String,
    val createdAtEpochMillis: Long,
    val attemptCount: Int = 0,
    val lastAttemptAtEpochMillis: Long? = null,
)
