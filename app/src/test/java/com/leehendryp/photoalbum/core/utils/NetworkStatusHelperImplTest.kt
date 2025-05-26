package com.leehendryp.photoalbum.core.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.test.core.app.ApplicationProvider
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.shadows.ShadowConnectivityManager
import org.robolectric.shadows.ShadowNetwork
import org.robolectric.shadows.ShadowNetworkCapabilities

@RunWith(RobolectricTestRunner::class)
class NetworkStatusHelperImplTest {

    private lateinit var context: Context
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var shadowConnectivityManager: ShadowConnectivityManager
    private lateinit var network: Network
    private lateinit var networkCapabilities: NetworkCapabilities
    private val helper: NetworkStatusHelper by lazy { NetworkStatusHelperImpl(connectivityManager) }
    private val callback = mockk<ConnectivityManager.NetworkCallback>()

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        shadowConnectivityManager = shadowOf(connectivityManager)

        network = ShadowNetwork.newInstance(1)
        networkCapabilities = ShadowNetworkCapabilities.newInstance()
        shadowOf(networkCapabilities).removeTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        shadowOf(connectivityManager)
            .setNetworkCapabilities(connectivityManager.activeNetwork, networkCapabilities)
    }

    @Test
    fun `when there is connection, hasConnection should return false`() = runTest {
        shadowOf(connectivityManager)
            .setNetworkCapabilities(connectivityManager.activeNetwork, networkCapabilities)

        val result = helper.hasConnection()

        assertThat(result, `is`(false))
    }

    @Test
    fun `when there is connection, hasConnection should return true`() = runTest {
        shadowOf(networkCapabilities).addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        shadowOf(connectivityManager)
            .setNetworkCapabilities(connectivityManager.activeNetwork, networkCapabilities)

        val result = helper.hasConnection()

        assertThat(result, `is`(true))
    }

    @Test
    fun `when callback is registered, should register on connectivity manager`() = runTest {
        val connectivityManager = mockk<ConnectivityManager>(relaxed = true)

        NetworkStatusHelperImpl(connectivityManager).register(callback)

        verify(exactly = 1) { connectivityManager.registerDefaultNetworkCallback(callback) }
    }

    @Test
    fun `when callback is unregistered, should unregister on connectivity manager`() = runTest {
        val connectivityManager = mockk<ConnectivityManager>(relaxed = true)

        NetworkStatusHelperImpl(connectivityManager).unregister(callback)

        verify(exactly = 1) { connectivityManager.unregisterNetworkCallback(callback) }
    }
}
