package com.farpost.mobile.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// TODO: style guide §2 calls for Inter (400/500/600/800). No bundled font files
// exist in this scaffold yet — swap FontFamily.Default below for a FontFamily
// built from res/font/*.ttf once real Inter files are added. Keep them bundled
// rather than fetched via the Google Fonts provider — this app is offline-first,
// and a network font fetch on first launch would fight that.
val FarpostFontFamily = FontFamily.Default

// Type scale — inferred/derived from real usage, not a formal system (§2).
val Typography = Typography(
    headlineSmall = TextStyle( // page/section heading
        fontFamily = FarpostFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
    ),
    titleMedium = TextStyle( // card/row title
        fontFamily = FarpostFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
    ),
    bodyMedium = TextStyle( // body
        fontFamily = FarpostFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
    ),
    labelLarge = TextStyle( // nav link / button label
        fontFamily = FarpostFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
    ),
    labelSmall = TextStyle( // meta / secondary text
        fontFamily = FarpostFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        color = TextSecondary,
    ),
    bodySmall = TextStyle( // empty-state text
        fontFamily = FarpostFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = TextMuted,
    ),
    displaySmall = TextStyle( // wordmark ("far" + "post")
        fontFamily = FarpostFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 22.sp,
        letterSpacing = (-0.3).sp,
    ),
)
