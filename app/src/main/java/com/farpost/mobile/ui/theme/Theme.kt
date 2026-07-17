package com.farpost.mobile.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Deliberately one color scheme, not light/dark — the real Farpost web app has
// no dark mode today (style guide §0). isSystemInDarkTheme() is intentionally
// unused; revisit only as a deliberate product decision, not a default.
private val FarpostColorScheme = lightColorScheme(
    primary = Brand,
    onPrimary = Color.White,
    secondary = LightChipBackground,
    onSecondary = LightChipText,
    background = PageBackground,
    onBackground = TextPrimary,
    surface = SurfaceWhite,
    onSurface = TextPrimary,
    surfaceVariant = HoverSurface,
    onSurfaceVariant = TextSecondary,
    outline = BorderDefault,
    error = Emergency,
)

@Composable
fun FarpostTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = FarpostColorScheme,
        typography = Typography,
        shapes = FarpostShapes,
        content = content,
    )
}
