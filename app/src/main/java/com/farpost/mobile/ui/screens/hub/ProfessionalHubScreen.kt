package com.farpost.mobile.ui.screens.hub

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.farpost.mobile.data.auth.ProfessionalProfile
import com.farpost.mobile.data.auth.ProfessionalRoles
import com.farpost.mobile.ui.SessionViewModel
import com.farpost.mobile.ui.components.FarpostListRow
import com.farpost.mobile.ui.components.FarpostTopBar

private data class RoleAction(val title: String, val meta: String, val onOpen: (() -> Unit)?)

/** One row per role in [ProfessionalProfile.roles] — this list grows as real
 * screens get built for other roles (adjuster, contractor, broker, ...). Scout is
 * not special-cased as the app's default destination; it's just the first role
 * with a real screen behind it. */
private fun roleAction(role: String, onOpenScoutCapture: () -> Unit): RoleAction = when (role) {
    ProfessionalRoles.SCOUT -> RoleAction(
        title = "Scout capture",
        meta = "Document a building or property",
        onOpen = onOpenScoutCapture,
    )
    else -> RoleAction(
        title = role.replace('_', ' ').replaceFirstChar { it.uppercase() },
        meta = "Coming soon",
        onOpen = null,
    )
}

@Composable
fun ProfessionalHubScreen(
    professional: ProfessionalProfile?,
    onOpenScoutCapture: () -> Unit,
    viewModel: SessionViewModel = hiltViewModel(),
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        FarpostTopBar(onSignOut = viewModel::signOut)

        Column(
            modifier = Modifier.padding(top = 24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            val roles = professional?.roles.orEmpty()
            if (roles.isEmpty()) {
                Text(
                    text = "No roles on this account.",
                    style = MaterialTheme.typography.bodySmall,
                )
            } else {
                roles.forEach { role ->
                    val action = roleAction(role, onOpenScoutCapture)
                    FarpostListRow(
                        title = action.title,
                        meta = action.meta,
                        actionLabel = if (action.onOpen != null) "Open →" else null,
                        onClick = action.onOpen,
                    )
                }
            }
        }
    }
}
