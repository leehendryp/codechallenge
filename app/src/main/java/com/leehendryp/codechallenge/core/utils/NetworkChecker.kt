package com.leehendryp.codechallenge.core.utils

import android.net.ConnectivityManager
import android.net.Network
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

internal class NetworkChecker @Inject constructor(
    private val registrar: NetworkStatusHelper,
) {

    fun isConnected(): Flow<Boolean> = callbackFlow {
        trySend(registrar.hasConnection())

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(true)
            }

            override fun onLost(network: Network) {
                trySend(false)
            }
        }

        registrar.register(callback)
        awaitClose { registrar.unregister(callback) }
    }.distinctUntilChanged()
}
