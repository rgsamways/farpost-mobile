package com.farpost.mobile.ui.theme

import androidx.compose.ui.graphics.Color

// Brand — style guide §1
val Brand = Color(0xFFF97316)
val BrandDark = Color(0xFFEA6C05)

// Surface — the real, live (light-only) theme. There is no dark mode today;
// do not add a dark color scheme without a deliberate product decision first.
val PageBackground = Color(0xFFF8FAFC) // slate-50
val SurfaceWhite = Color(0xFFFFFFFF)
val TextPrimary = Color(0xFF0F172A) // slate-900
val TextSecondary = Color(0xFF64748B) // slate-500
val TextMuted = Color(0xFF94A3B8) // slate-400
val BorderDefault = Color(0xFFE2E8F0) // slate-200
val HoverSurface = Color(0xFFF1F5F9) // slate-100

// Chip buttons (sign-in / sign-out pattern, §6)
val DarkChipBackground = Color(0xFF0F172A) // slate-900
val LightChipBackground = Color(0xFFF1F5F9) // slate-100
val LightChipText = Color(0xFF334155) // slate-700

// Urgency (claim/job priority) — §1
val Emergency = Color(0xFFEF4444)
val Urgent = Brand
val Routine = Color(0xFF22C55E)

// Status badge palette — dark chip on light page, deliberate per §1.
data class StatusColor(val background: Color, val text: Color, val border: Color)

object StatusColors {
    val Red = StatusColor(Color(0xFF450A0A), Color(0xFFFCA5A5), Color(0xFF991B1B))
    val Orange = StatusColor(Color(0xFF431407), Color(0xFFFDBA74), Color(0xFF9A3412))
    val Green = StatusColor(Color(0xFF052E16), Color(0xFF86EFAC), Color(0xFF166534))
    val Blue = StatusColor(Color(0xFF172554), Color(0xFF93C5FD), Color(0xFF1E40AF))
    val Yellow = StatusColor(Color(0xFF422006), Color(0xFFFDE047), Color(0xFF854D0E))
    val Teal = StatusColor(Color(0xFF042F2E), Color(0xFF5EEAD4), Color(0xFF115E59))
    val Indigo = StatusColor(Color(0xFF1E1B4B), Color(0xFFA5B4FC), Color(0xFF3730A3))
    val Purple = StatusColor(Color(0xFF3B0764), Color(0xFFD8B4FE), Color(0xFF6B21A8))
    val Slate = StatusColor(Color(0xFF1E293B), Color(0xFF94A3B8), Color(0xFF334155))
}
