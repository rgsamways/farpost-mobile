package com.farpost.mobile.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Radii — style guide §3. 8dp is the dominant radius (cards, buttons, inputs,
// nav links); 12dp is reserved for the nav's auth chip and the login-style card.
val RadiusDefault = 8.dp
val RadiusChip = 12.dp
val RadiusPill = 50.dp

val FarpostShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(RadiusDefault),
    medium = RoundedCornerShape(RadiusDefault),
    large = RoundedCornerShape(RadiusChip),
    extraLarge = RoundedCornerShape(16.dp),
)
