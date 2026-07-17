package com.farpost.mobile.di

import android.content.Context
import androidx.room.Room
import com.farpost.mobile.data.local.db.FarpostDatabase
import com.farpost.mobile.data.local.db.dao.PendingOperationDao
import com.farpost.mobile.data.local.db.dao.ScoutEntryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideFarpostDatabase(@ApplicationContext context: Context): FarpostDatabase =
        Room.databaseBuilder(context, FarpostDatabase::class.java, "farpost.db").build()

    @Provides
    fun provideScoutEntryDao(database: FarpostDatabase): ScoutEntryDao = database.scoutEntryDao()

    @Provides
    fun providePendingOperationDao(database: FarpostDatabase): PendingOperationDao =
        database.pendingOperationDao()
}
