package com.leehendryp.codechallenge.core.utils

import android.net.ConnectivityManager
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class NetworkCheckerTest {

    private lateinit var registrar: NetworkStatusHelper
    private lateinit var checker: NetworkCheckerImpl

    @Before
    fun setup() {
        registrar = mockk(relaxed = true)
        checker = NetworkCheckerImpl(registrar)
    }

    @Test
    fun `when current network status is unavailable, should emit false`() = runTest {
        val result = checker.isConnected().first()

        assertThat(result, `is`(false))
    }

    @Test
    fun `when current network status is available, should emit true`() = runTest {
        every { registrar.hasConnection() } returns true

        val result = checker.isConnected().first()

        assertThat(result, `is`(true))
    }

    /*
    Lee May 12, 2025:
    Using just `val result = checker.isConnected().first()` suspends the current test coroutine until
    a value is emitted, which blocks further test setup, including capturing and invoking the network
    callback. Basically, since it suspends before a value is emitted, no callback is ever captured.

    However, `async { checker.isConnected().first() }` runs the collection in a separate coroutine,
    allowing the test to proceed, capture the callback, and invoke it. `advanceUntilIdle()` ensures
    the flow is collected before the callback is triggered.

    A similar result could be reach with a var + assignment from a launch, but it is too verbose.
     */

    @Test
    fun `when last emission is the same as before, should not emit again`() = runTest {
        val callbackSlot = slot<ConnectivityManager.NetworkCallback>()
        every { registrar.hasConnection() } returns true
        every { registrar.register(capture(callbackSlot)) } answers { }

        val expected = listOf(true, false, true)
        val result = async { checker.isConnected().take(expected.size).toList() }
        advanceUntilIdle()
        callbackSlot.captured.onLost(mockk())
        callbackSlot.captured.onLost(mockk())
        callbackSlot.captured.onAvailable(mockk())

        assertThat(result.await(), `is`(expected))
    }

    @Test
    fun `when network becomes available, should emit true`() = runTest {
        val callbackSlot = slot<ConnectivityManager.NetworkCallback>()
        every { registrar.register(capture(callbackSlot)) } answers { }

        val expected = listOf(false, true)
        val result = async { checker.isConnected().take(expected.size).toList() }
        advanceUntilIdle()
        callbackSlot.captured.onAvailable(mockk())

        assertThat(result.await(), `is`(expected))
    }

    @Test
    fun `when network is lost, should emit false`() = runTest {
        val callbackSlot = slot<ConnectivityManager.NetworkCallback>()
        every { registrar.hasConnection() } returns true
        every { registrar.register(capture(callbackSlot)) } answers { }

        val expected = listOf(true, false)
        val result = async { checker.isConnected().take(expected.size).toList() }
        advanceUntilIdle()
        callbackSlot.captured.onLost(mockk())

        assertThat(result.await(), `is`(expected))
    }

    @Test
    fun `when network status changes, should emit booleans accordingly`() = runTest {
        val callbackSlot = slot<ConnectivityManager.NetworkCallback>()
        every { registrar.register(capture(callbackSlot)) } answers { }

        val expected = listOf(false, true, false)
        val result = async { checker.isConnected().take(expected.size).toList() }
        advanceUntilIdle()
        callbackSlot.captured.onAvailable(mockk())
        callbackSlot.captured.onLost(mockk())

        assertThat(result.await(), equalTo(expected))
    }
}
