package com.farpost.mobile.data.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.farpost.mobile.data.local.db.entity.PendingOperationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PendingOperationDao {
    @Insert
    suspend fun insert(operation: PendingOperationEntity)

    @Query("SELECT * FROM pending_operations ORDER BY createdAtEpochMillis ASC")
    suspend fun getAllPending(): List<PendingOperationEntity>

    @Query("SELECT * FROM pending_operations ORDER BY createdAtEpochMillis ASC")
    fun observePending(): Flow<List<PendingOperationEntity>>

    @Query("UPDATE pending_operations SET attemptCount = attemptCount + 1, lastAttemptAtEpochMillis = :attemptedAt WHERE id = :id")
    suspend fun recordAttempt(id: String, attemptedAt: Long)

    @Delete
    suspend fun delete(operation: PendingOperationEntity)
}
