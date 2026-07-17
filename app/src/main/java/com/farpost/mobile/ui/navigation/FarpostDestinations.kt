package com.farpost.mobile.ui.navigation

import kotlinx.serialization.Serializable

// Type-safe (serializable) routes — Navigation-Compose 2.8+. Avoids the older
// string-route convention where destination args are stringly-typed.

@Serializable
object Welcome

// One destination per capability (mirrors farpost-web's /admin, /hub, /b/[slug]
// split) — a signed-in landing is never scout-specific, since scout is only ever
// one of many roles inside ProfessionalHub.
@Serializable
object AdminHome

@Serializable
object ProfessionalHub

@Serializable
object OwnerHome

@Serializable
object ScoutCapture
