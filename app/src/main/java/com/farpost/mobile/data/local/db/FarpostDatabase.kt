package com.farpost.mobile.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.farpost.mobile.data.local.db.dao.PendingOperationDao
import com.farpost.mobile.data.local.db.dao.ScoutEntryDao
import com.farpost.mobile.data.local.db.entity.PendingOperationEntity
import com.farpost.mobile.data.local.db.entity.ScoutEntryEntity

@Database(
    entities = [PendingOperationEntity::class, ScoutEntryEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class FarpostDatabase : RoomDatabase() {
    abstract fun pendingOperationDao(): PendingOperationDao
    abstract fun scoutEntryDao(): ScoutEntryDao
}
