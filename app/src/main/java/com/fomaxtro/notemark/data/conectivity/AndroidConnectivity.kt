package com.fomaxtro.notemark.data.conectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.fomaxtro.notemark.domain.conectivity.Connectivity
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AndroidConnectivity(context: Context) : Connectivity {
    private val connectivityManager = context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager

    override fun hasInternetConnection(): Flow<Boolean> = callbackFlow {
        val currentNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(currentNetwork)

        val hasInternet = networkCapabilities?.let { networkCapabilities ->
            hasInternetConnection(networkCapabilities)
        } ?: false

        trySend(hasInternet)

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                super.onCapabilitiesChanged(network, networkCapabilities)

                trySend(hasInternetConnection(networkCapabilities))
            }

            override fun onLost(network: Network) {
                super.onLost(network)

                trySend(false)
            }
        }

        connectivityManager.requestNetwork(networkRequest, networkCallback)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }

    private fun hasInternetConnection(networkCapabilities: NetworkCapabilities): Boolean {
        val hasInternet = networkCapabilities
            .hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        val hasValidatedInternet = networkCapabilities
            .hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)

        return hasInternet && hasValidatedInternet
    }
}