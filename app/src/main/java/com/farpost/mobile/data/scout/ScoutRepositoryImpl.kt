package com.farpost.mobile.data.scout

import androidx.room.withTransaction
import com.farpost.mobile.data.local.db.FarpostDatabase
import com.farpost.mobile.data.local.db.entity.PendingOperationEntity
import com.farpost.mobile.data.local.db.entity.ScoutEntryEntity
import com.farpost.mobile.sync.EntityTypes
import com.farpost.mobile.sync.SyncScheduler
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private const val OPERATION_CREATE = "CREATE"

class ScoutRepositoryImpl @Inject constructor(
    private val database: FarpostDatabase,
    private val syncScheduler: SyncScheduler,
) : ScoutRepository {

    private val json = Json

    override fun observeEntries(): Flow<List<ScoutEntryEntity>> =
        database.scoutEntryDao().observeAll()

    override suspend fun captureEntry(note: String, buildingId: String?, photoUri: String?) {
        val now = System.currentTimeMillis()
        // Client-generated id doubles as the outbox operation's idempotency key
        // (see PendingOperationEntity kdoc) — the backend must dedupe on it once
        // this is wired to a real sync endpoint.
        val entry = ScoutEntryEntity(
            id = UUID.randomUUID().toString(),
            buildingId = buildingId,
            note = note,
            photoUri = photoUri,
            capturedAtEpochMillis = now,
            isSynced = false,
        )
        val outboxOperation = PendingOperationEntity(
            id = entry.id,
            entityType = EntityTypes.SCOUT_ENTRY,
            entityId = entry.id,
            operationType = OPERATION_CREATE,
            payload = json.encodeToString(entry),
            createdAtEpochMillis = now,
        )
        database.withTransaction {
            database.scoutEntryDao().upsert(entry)
            database.pendingOperationDao().insert(outboxOperation)
        }
        // Best-effort — WorkManager only actually runs this once NetworkType.CONNECTED
        // is satisfied, so this is safe to call unconditionally even fully offline.
        syncScheduler.triggerImmediateSync()
    }
}
