package com.farpost.mobile

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.farpost.mobile.sync.SyncScheduler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class FarpostApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var syncScheduler: SyncScheduler

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        syncScheduler.schedulePeriodicSync()
        registerSyncOnConnectivity()
    }

    /** WorkManager's own NetworkType.CONNECTED constraint only gets checked when a
     * work request is (re-)evaluated — it doesn't react the instant connectivity
     * actually returns. This callback closes that gap: the moment the network comes
     * back, trigger a sync attempt immediately instead of waiting on the periodic
     * 15-minute tick (which stays as the restart-safety backstop). */
    private fun registerSyncOnConnectivity() {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(
            request,
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    syncScheduler.triggerImmediateSync()
                }
            },
        )
    }
}
