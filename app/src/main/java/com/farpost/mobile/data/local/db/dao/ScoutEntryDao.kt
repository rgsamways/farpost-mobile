package com.farpost.mobile.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.farpost.mobile.data.local.db.entity.ScoutEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoutEntryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entry: ScoutEntryEntity)

    @Query("SELECT * FROM scout_entries ORDER BY capturedAtEpochMillis DESC")
    fun observeAll(): Flow<List<ScoutEntryEntity>>

    @Query("UPDATE scout_entries SET isSynced = 1 WHERE id = :id")
    suspend fun markSynced(id: String)
}
