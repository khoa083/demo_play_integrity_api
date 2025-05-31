package com.kblack.base.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * Enum representing different network connection types.
 */
enum class NetworkType {
    NONE, WIFI, CELLULAR, VPN, ETHERNET, OTHER
}

/**
 * Interface for network monitoring.
 */
interface NetworkMonitor {
    /**
     * Checks if a network is currently connected.
     */
    suspend fun isNetworkConnected(): Boolean

    /**
     * Gets the current network type.
     */
    suspend fun getNetworkType(): NetworkType

    /**
     * Observes network connectivity changes as a Flow.
     */
    fun observeNetworkStatus(): Flow<Boolean>

    /**
     * Observes network type changes as a Flow.
     */
    fun observeNetworkType(): Flow<NetworkType>
}

/**
 * Implementation of NetworkMonitor for checking and observing network status.
 */
class NetworkMonitorImpl(private val context: Context) : NetworkMonitor {
    private val connectivityManager: ConnectivityManager by lazy {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    override suspend fun isNetworkConnected(): Boolean {
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    override suspend fun getNetworkType(): NetworkType {
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return when {
            capabilities == null -> NetworkType.NONE
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkType.WIFI
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkType.CELLULAR
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> NetworkType.VPN
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> NetworkType.ETHERNET
            else -> NetworkType.OTHER
        }
    }

    override fun observeNetworkStatus(): Flow<Boolean> = callbackFlow {
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(true)
            }

            override fun onLost(network: Network) {
                trySend(false)
            }

            override fun onUnavailable() {
                trySend(false)
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(request, networkCallback)
        // Initial state
        trySend(isNetworkConnected())

        awaitClose {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }.distinctUntilChanged()

    override fun observeNetworkType(): Flow<NetworkType> = callbackFlow {
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                val type = when {
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkType.WIFI
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkType.CELLULAR
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> NetworkType.VPN
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> NetworkType.ETHERNET
                    else -> NetworkType.OTHER
                }
                trySend(type)
            }

            override fun onLost(network: Network) {
                trySend(NetworkType.NONE)
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(request, networkCallback)
        // Initial state
        trySend(getNetworkType())

        awaitClose {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }.distinctUntilChanged()
}