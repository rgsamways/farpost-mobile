package com.farpost.mobile.ui.screens.admin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.farpost.mobile.data.auth.AdminProfile
import com.farpost.mobile.ui.SessionViewModel
import com.farpost.mobile.ui.components.FarpostTopBar

@Composable
fun AdminHomeScreen(
    admin: AdminProfile?,
    viewModel: SessionViewModel = hiltViewModel(),
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        FarpostTopBar(onSignOut = viewModel::signOut)

        Column(modifier = Modifier.padding(top = 24.dp)) {
            Text(
                text = "Signed in as ${admin?.name.orEmpty()}",
                style = MaterialTheme.typography.titleMedium,
            )
            if (admin?.isSuperAdmin == true) {
                Text(
                    text = "Super admin",
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}
