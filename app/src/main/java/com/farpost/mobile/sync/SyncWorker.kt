package com.farpost.mobile.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.farpost.mobile.data.local.db.FarpostDatabase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CancellationException

/**
 * Outbox replay — dispatches each pending operation to the [EntrySyncHandler]
 * registered for its entityType (see SyncHandlerModule), rather than a growing
 * when-block. An entityType with no registered handler is a logged failure, not a
 * silent drop or a crash — surfaces a missing-wiring bug instead of hiding it.
 */
@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val database: FarpostDatabase,
    private val entrySyncHandlers: Map<String, @JvmSuppressWildcards EntrySyncHandler>,
    private val syncStatusRepository: SyncStatusRepository,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val pending = database.pendingOperationDao().getAllPending()
        if (pending.isEmpty()) {
            syncStatusRepository.update(SyncStatus.Idle)
            return Result.success()
        }

        syncStatusRepository.update(SyncStatus.Syncing)

        return try {
            var anyFailure = false
            for (operation in pending) {
                val handler = entrySyncHandlers[operation.entityType]
                val outcome = handler?.sync(operation)
                    ?: SyncOutcome.Failure("no sync handler registered for entityType=${operation.entityType}")

                when (outcome) {
                    is SyncOutcome.Success, is SyncOutcome.Conflict -> {
                        database.pendingOperationDao().delete(operation)
                    }
                    is SyncOutcome.Failure -> {
                        anyFailure = true
                        database.pendingOperationDao().recordAttempt(operation.id, System.currentTimeMillis())
                    }
                }
            }

            syncStatusRepository.update(if (anyFailure) SyncStatus.Partial else SyncStatus.Idle)
            if (anyFailure) Result.retry() else Result.success()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            syncStatusRepository.update(SyncStatus.Error)
            Result.retry()
        }
    }
}
