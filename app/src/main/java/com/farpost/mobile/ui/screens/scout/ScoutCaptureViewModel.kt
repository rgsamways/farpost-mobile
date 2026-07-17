package com.farpost.mobile.ui.screens.scout

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farpost.mobile.data.local.db.entity.ScoutEntryEntity
import com.farpost.mobile.data.scout.ScoutRepository
import com.farpost.mobile.sync.SyncStatus
import com.farpost.mobile.sync.SyncStatusRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ScoutCaptureViewModel @Inject constructor(
    private val scoutRepository: ScoutRepository,
    syncStatusRepository: SyncStatusRepository,
) : ViewModel() {

    val entries: StateFlow<List<ScoutEntryEntity>> = scoutRepository.observeEntries()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val syncStatus: StateFlow<SyncStatus> = syncStatusRepository.status

    var noteInput by mutableStateOf("")
        private set

    fun onNoteChange(value: String) {
        noteInput = value
    }

    /** Local-first: returns as soon as the write lands in Room, never waits on
     * network — see ScoutRepositoryImpl. */
    fun capture() {
        val note = noteInput
        if (note.isBlank()) return
        viewModelScope.launch {
            scoutRepository.captureEntry(note = note, buildingId = null, photoUri = null)
            noteInput = ""
        }
    }
}
