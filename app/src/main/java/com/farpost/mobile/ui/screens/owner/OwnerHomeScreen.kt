package com.farpost.mobile.ui.screens.owner

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.farpost.mobile.data.auth.OwnerProfile
import com.farpost.mobile.ui.SessionViewModel
import com.farpost.mobile.ui.components.FarpostTopBar

@Composable
fun OwnerHomeScreen(
    owner: OwnerProfile?,
    buildingSlug: String?,
    viewModel: SessionViewModel = hiltViewModel(),
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        FarpostTopBar(onSignOut = viewModel::signOut)

        Column(modifier = Modifier.padding(top = 24.dp)) {
            Text(
                text = "Signed in as ${owner?.name.orEmpty()}",
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = if (buildingSlug != null) "Building: $buildingSlug" else "No verified building on this account.",
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}
