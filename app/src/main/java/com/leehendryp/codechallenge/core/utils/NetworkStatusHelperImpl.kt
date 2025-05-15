package com.leehendryp.codechallenge.core.utils

import android.net.ConnectivityManager
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_ETHERNET
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import javax.inject.Inject

interface NetworkStatusHelper {
    fun hasConnection(): Boolean
    fun register(callback: ConnectivityManager.NetworkCallback)
    fun unregister(callback: ConnectivityManager.NetworkCallback)
}

internal class NetworkStatusHelperImpl @Inject constructor(
    private val connectivityManager: ConnectivityManager,
) : NetworkStatusHelper {

    override fun hasConnection(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return listOf(TRANSPORT_WIFI, TRANSPORT_CELLULAR, TRANSPORT_ETHERNET)
            .any { capabilities.hasTransport(it) }
    }

    override fun register(callback: ConnectivityManager.NetworkCallback) {
        connectivityManager.registerDefaultNetworkCallback(callback)
    }

    override fun unregister(callback: ConnectivityManager.NetworkCallback) {
        connectivityManager.unregisterNetworkCallback(callback)
    }
}
