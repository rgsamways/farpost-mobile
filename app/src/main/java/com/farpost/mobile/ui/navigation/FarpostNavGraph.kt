package com.farpost.mobile.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.farpost.mobile.data.auth.Capabilities
import com.farpost.mobile.data.auth.SessionState
import com.farpost.mobile.data.auth.UserSession
import com.farpost.mobile.data.auth.has
import com.farpost.mobile.ui.screens.admin.AdminHomeScreen
import com.farpost.mobile.ui.screens.hub.ProfessionalHubScreen
import com.farpost.mobile.ui.screens.owner.OwnerHomeScreen
import com.farpost.mobile.ui.screens.scout.ScoutCaptureScreen
import com.farpost.mobile.ui.screens.welcome.WelcomeScreen

/** A signed-in account may hold more than one capability; when it does, priority
 * is admin > professional > owner — mirrors farpost-web's login page redirect
 * cascade (`/admin` > `/hub` > `/b/[slug]`), which is the reference implementation
 * for post-login routing against this same endpoint. */
private fun destinationFor(session: UserSession): Any = when {
    session.has(Capabilities.ADMIN) -> AdminHome
    session.has(Capabilities.PROFESSIONAL) -> ProfessionalHub
    session.has(Capabilities.OWNER) -> OwnerHome
    else -> Welcome
}

@Composable
fun FarpostNavGraph(
    sessionState: SessionState,
    navController: NavHostController = rememberNavController(),
) {
    // Loading is only ever the persisted-session read on cold start (see
    // SessionState kdoc) — nothing to navigate yet, so don't build a graph for it.
    if (sessionState is SessionState.Loading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    // The very first emission here just confirms what startDestination (below)
    // already set up — only react to sessionState actually *changing* afterwards
    // (sign-in while on Welcome, or sign-out from any signed-in destination).
    var hasSeenInitialState by remember { mutableStateOf(false) }
    LaunchedEffect(sessionState) {
        if (!hasSeenInitialState) {
            hasSeenInitialState = true
            return@LaunchedEffect
        }
        val target = if (sessionState is SessionState.SignedIn) destinationFor(sessionState.session) else Welcome
        navController.navigate(target) {
            popUpTo(navController.graph.id) { inclusive = true }
            launchSingleTop = true
        }
    }

    val signedInSession = (sessionState as? SessionState.SignedIn)?.session

    NavHost(
        navController = navController,
        startDestination = if (signedInSession != null) destinationFor(signedInSession) else Welcome,
    ) {
        composable<Welcome> {
            WelcomeScreen()
        }
        composable<AdminHome> {
            AdminHomeScreen(admin = signedInSession?.admin)
        }
        composable<ProfessionalHub> {
            ProfessionalHubScreen(
                professional = signedInSession?.professional,
                onOpenScoutCapture = { navController.navigate(ScoutCapture) },
            )
        }
        composable<OwnerHome> {
            OwnerHomeScreen(owner = signedInSession?.owner, buildingSlug = signedInSession?.buildingSlug)
        }
        composable<ScoutCapture> {
            ScoutCaptureScreen(onBack = { navController.popBackStack() })
        }
    }
}
