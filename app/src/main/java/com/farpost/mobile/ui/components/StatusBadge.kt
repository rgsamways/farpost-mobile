package com.farpost.mobile.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.farpost.mobile.ui.theme.RadiusPill
import com.farpost.mobile.ui.theme.StatusColor
import com.farpost.mobile.ui.theme.StatusColors

/** Claim/job/inspection lifecycle status. Colors per style guide §1 — this table is
 * real and current (unlike the web app's stale `Card`/`Input` components), reuse verbatim. */
enum class FarpostStatus(val label: String, val colors: StatusColor) {
    Emergency("Emergency", StatusColors.Red),
    Failed("Failed", StatusColors.Red),
    Suspended("Suspended", StatusColors.Red),
    Urgent("Urgent", StatusColors.Orange),
    Routine("Routine", StatusColors.Green),
    Active("Active", StatusColors.Green),
    New("New", StatusColors.Blue),
    Dispatched("Dispatched", StatusColors.Yellow),
    Pending("Pending", StatusColors.Yellow),
    Accepted("Accepted", StatusColors.Teal),
    InProgress("In progress", StatusColors.Indigo),
    Documented("Documented", StatusColors.Purple),
    Closed("Closed", StatusColors.Slate),
}

@Composable
fun StatusBadge(status: FarpostStatus, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = status.colors.background,
        shape = RoundedCornerShape(RadiusPill),
        border = BorderStroke(1.dp, status.colors.border),
    ) {
        Text(
            text = status.label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold),
            color = status.colors.text,
        )
    }
}
