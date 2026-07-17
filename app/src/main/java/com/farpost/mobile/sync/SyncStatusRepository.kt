package com.farpost.mobile.sync

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** Published by [SyncWorker], observed by any screen that wants real sync health
 * instead of a queue-depth guess (e.g. ScoutCaptureScreen's Synced/Unsynced label). */
@Singleton
class SyncStatusRepository @Inject constructor() {
    private val _status = MutableStateFlow<SyncStatus>(SyncStatus.Idle)
    val status: StateFlow<SyncStatus> = _status.asStateFlow()

    fun update(status: SyncStatus) {
        _status.value = status
    }
}
