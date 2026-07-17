package com.farpost.mobile.di

import com.farpost.mobile.sync.EntityTypes
import com.farpost.mobile.sync.EntrySyncHandler
import com.farpost.mobile.sync.ScoutEntrySyncHandler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

/** Registers one [EntrySyncHandler] per syncable entityType into a
 * Map<String, EntrySyncHandler> — SyncWorker dispatches by lookup on that map, so a
 * new syncable entity is added here, not as a new branch in SyncWorker itself. */
@Module
@InstallIn(SingletonComponent::class)
abstract class SyncHandlerModule {
    @Binds
    @IntoMap
    @StringKey(EntityTypes.SCOUT_ENTRY)
    abstract fun bindScoutEntrySyncHandler(impl: ScoutEntrySyncHandler): EntrySyncHandler
}
