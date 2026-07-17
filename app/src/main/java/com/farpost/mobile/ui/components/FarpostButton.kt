package com.farpost.mobile.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.farpost.mobile.ui.theme.Brand
import com.farpost.mobile.ui.theme.DarkChipBackground
import com.farpost.mobile.ui.theme.LightChipBackground
import com.farpost.mobile.ui.theme.LightChipText
import com.farpost.mobile.ui.theme.RadiusChip
import com.farpost.mobile.ui.theme.RadiusDefault

/** Style guide §6: the shared component library's secondary/outline/ghost variants
 * still assume the web app's dead dark theme — these three variants instead follow
 * what the real, live nav and pages actually do. */
enum class FarpostButtonVariant { Default, DarkChip, LightChip }

enum class FarpostButtonSize(val height: Dp, val horizontalPadding: Dp) {
    Small(32.dp, 12.dp),
    Medium(40.dp, 16.dp),
    Large(48.dp, 24.dp),
    ExtraLarge(56.dp, 32.dp),
}

@Composable
fun FarpostButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: FarpostButtonVariant = FarpostButtonVariant.Default,
    size: FarpostButtonSize = FarpostButtonSize.Medium,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    // active:scale-[0.98] — style guide §5.
    val scale by animateFloatAsState(if (isPressed) 0.98f else 1f, label = "buttonPressScale")

    val (background, contentColor, shape) = when (variant) {
        FarpostButtonVariant.Default -> Triple(Brand, Color.White, RoundedCornerShape(RadiusDefault))
        FarpostButtonVariant.DarkChip -> Triple(DarkChipBackground, Color.White, RoundedCornerShape(RadiusChip))
        FarpostButtonVariant.LightChip -> Triple(LightChipBackground, LightChipText, RoundedCornerShape(RadiusChip))
    }

    Surface(
        modifier = modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .height(size.height)
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick),
        color = background,
        contentColor = contentColor,
        shape = shape,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = size.horizontalPadding),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = contentColor,
            )
        }
    }
}
