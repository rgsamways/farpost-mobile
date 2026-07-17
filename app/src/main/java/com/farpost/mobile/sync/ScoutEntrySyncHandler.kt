package com.farpost.mobile.sync

import com.farpost.mobile.data.local.db.dao.ScoutEntryDao
import com.farpost.mobile.data.local.db.entity.PendingOperationEntity
import com.farpost.mobile.data.local.db.entity.ScoutEntryEntity
import com.farpost.mobile.data.remote.FarpostApi
import com.farpost.mobile.data.remote.ScoutEntrySubmitRequest
import java.io.IOException
import java.time.Instant
import javax.inject.Inject
import kotlinx.serialization.json.Json
import retrofit2.HttpException

/** Dispatches [EntityTypes.SCOUT_ENTRY] pending operations to the real
 * POST /api/v1/professional/scout-entries endpoint — see farpost-api's
 * scout-entry-sync-endpoint change. */
class ScoutEntrySyncHandler @Inject constructor(
    private val api: FarpostApi,
    private val scoutEntryDao: ScoutEntryDao,
) : EntrySyncHandler {

    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun sync(operation: PendingOperationEntity): SyncOutcome {
        val entry = try {
            json.decodeFromString<ScoutEntryEntity>(operation.payload)
        } catch (e: Exception) {
            return SyncOutcome.Failure("undecodable payload: ${e.message}")
        }

        return try {
            api.submitScoutEntry(
                ScoutEntrySubmitRequest(
                    id = entry.id,
                    buildingId = entry.buildingId,
                    note = entry.note,
                    photoUrl = entry.photoUri,
                    capturedAt = Instant.ofEpochMilli(entry.capturedAtEpochMillis).toString(),
                ),
            )
            scoutEntryDao.markSynced(entry.id)
            SyncOutcome.Success
        } catch (e: HttpException) {
            // 409 ("already exists") is TapLog's ceiling for "done, don't loop
            // forever on a conflict" — not a real merge, just stop retrying.
            if (e.code() == 409) {
                scoutEntryDao.markSynced(entry.id)
                SyncOutcome.Conflict
            } else {
                SyncOutcome.Failure("HTTP ${e.code()}")
            }
        } catch (e: IOException) {
            SyncOutcome.Failure(e.message ?: "network error")
        }
    }
}
