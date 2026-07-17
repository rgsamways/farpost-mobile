package com.farpost.mobile.ui.screens.scout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.farpost.mobile.data.local.db.entity.ScoutEntryEntity
import com.farpost.mobile.sync.SyncStatus
import com.farpost.mobile.ui.components.FarpostButton
import com.farpost.mobile.ui.components.FarpostListRow
import com.farpost.mobile.ui.theme.Brand
import com.farpost.mobile.ui.theme.BorderDefault
import com.farpost.mobile.ui.theme.TextMuted
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ScoutCaptureScreen(
    onBack: () -> Unit,
    viewModel: ScoutCaptureViewModel = hiltViewModel(),
) {
    val entries by viewModel.entries.collectAsStateWithLifecycle()
    val syncStatus by viewModel.syncStatus.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
            }
            Text(text = "Scout capture", style = MaterialTheme.typography.headlineSmall)
        }

        if (syncStatus is SyncStatus.Syncing) {
            Text(
                text = "Syncing…",
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                modifier = Modifier.padding(start = 48.dp),
            )
        }

        Column(modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)) {
            OutlinedTextField(
                value = viewModel.noteInput,
                onValueChange = viewModel::onNoteChange,
                label = { Text("What did you observe?") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Brand,
                    unfocusedBorderColor = BorderDefault,
                ),
            )
            FarpostButton(
                label = "Capture",
                onClick = viewModel::capture,
                modifier = Modifier.padding(top = 12.dp),
            )
        }

        if (entries.isEmpty()) {
            Text(
                text = "No entries captured yet.",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
                color = TextMuted,
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 8.dp),
            ) {
                items(entries, key = ScoutEntryEntity::id) { entry ->
                    FarpostListRow(
                        title = entry.note,
                        meta = "${formatTimestamp(entry.capturedAtEpochMillis)} · ${if (entry.isSynced) "Synced" else "Unsynced"}",
                    )
                }
            }
        }
    }
}

private fun formatTimestamp(epochMillis: Long): String =
    SimpleDateFormat("MMM d, h:mm a", Locale.getDefault()).format(Date(epochMillis))
