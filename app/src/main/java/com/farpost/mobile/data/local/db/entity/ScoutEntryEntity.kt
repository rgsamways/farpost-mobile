package com.farpost.mobile.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * Sample offline-first entity: a single building/property walk observation captured
 * by a scout in the field. This is the concrete anchor case for the app's whole
 * offline architecture (see farpost-mobile-scope memory) — real fields will expand
 * once the backend's data model for this is settled, but the offline-first shape
 * (client-generated id, local-first write, isSynced flag) shouldn't need to change.
 *
 * @Serializable so it can be encoded directly as an outbox payload (see
 * [com.farpost.mobile.data.scout.ScoutRepositoryImpl]) without a parallel DTO.
 */
@Serializable
@Entity(tableName = "scout_entries")
data class ScoutEntryEntity(
    @PrimaryKey val id: String,
    val buildingId: String?,
    val note: String,
    val photoUri: String?,
    val capturedAtEpochMillis: Long,
    /** Surfaced in the UI as "unsynced" rather than hidden — style guide's
     * "Farpost notifies, it does not gate" applies to sync state too. */
    val isSynced: Boolean = false,
)
