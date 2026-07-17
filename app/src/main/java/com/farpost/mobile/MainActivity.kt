package com.farpost.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.farpost.mobile.ui.MainViewModel
import com.farpost.mobile.ui.navigation.FarpostNavGraph
import com.farpost.mobile.ui.theme.FarpostTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FarpostTheme {
                // targetSdk 35 makes edge-to-edge mandatory (no opt-in) — without this,
                // every screen's top content draws under the status bar, and taps there
                // land on the system status bar instead of the app.
                Surface(modifier = Modifier.fillMaxSize().safeDrawingPadding()) {
                    val viewModel: MainViewModel = viewModel()
                    val sessionState by viewModel.sessionState.collectAsStateWithLifecycle()
                    FarpostNavGraph(sessionState = sessionState)
                }
            }
        }
    }
}
