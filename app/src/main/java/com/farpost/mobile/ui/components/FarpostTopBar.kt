package com.farpost.mobile.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.farpost.mobile.ui.theme.Brand
import com.farpost.mobile.ui.theme.TextPrimary

/** Wordmark + sign-out chip — identical header across every signed-in destination
 * (AdminHome/ProfessionalHub/OwnerHome), so it lives here instead of being
 * duplicated per screen. */
@Composable
fun FarpostTopBar(onSignOut: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(color = TextPrimary, fontWeight = FontWeight.ExtraBold)) { append("far") }
                withStyle(SpanStyle(color = Brand, fontWeight = FontWeight.ExtraBold)) { append("post") }
            },
            style = MaterialTheme.typography.headlineSmall,
        )
        FarpostButton(
            label = "Sign out",
            onClick = onSignOut,
            variant = FarpostButtonVariant.LightChip,
            size = FarpostButtonSize.Small,
        )
    }
}
