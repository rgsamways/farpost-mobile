package com.farpost.mobile.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.farpost.mobile.ui.theme.Brand
import com.farpost.mobile.ui.theme.BorderDefault
import com.farpost.mobile.ui.theme.RadiusDefault
import com.farpost.mobile.ui.theme.SurfaceWhite

/**
 * The dominant "card" pattern across Farpost (list rows on the hub, inspection
 * sequences, etc.) — bordered row, not an elevated Card. Style guide §6: reuse
 * this for any list of items (recent visits, queued uploads), not [FarpostButton]
 * or Material's default elevated Card, which reads as a different, heavier
 * visual language than the rest of the product.
 */
@Composable
fun FarpostListRow(
    title: String,
    modifier: Modifier = Modifier,
    meta: String? = null,
    actionLabel: String? = null,
    onClick: (() -> Unit)? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val borderColor = if (isPressed) Brand else BorderDefault

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) {
                    Modifier.clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
                } else {
                    Modifier
                },
            ),
        color = SurfaceWhite,
        shape = RoundedCornerShape(RadiusDefault),
        border = BorderStroke(1.dp, borderColor),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                if (meta != null) {
                    Text(text = meta, style = MaterialTheme.typography.labelSmall)
                }
            }
            if (actionLabel != null) {
                Text(
                    text = actionLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = Brand,
                )
            }
        }
    }
}
