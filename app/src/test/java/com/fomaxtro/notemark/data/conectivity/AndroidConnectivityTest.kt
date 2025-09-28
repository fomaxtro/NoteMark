package com.fomaxtro.notemark.data.conectivity

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class AndroidConnectivityTest {
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var connectivity: AndroidConnectivity

    @BeforeEach
    fun setUp() {
        connectivityManager = mockk()
        connectivity = AndroidConnectivity(connectivityManager)
    }

    @ParameterizedTest
    @CsvSource(
        "false, false, false",
        "true, false, false",
        "false, true, false",
        "true, true, true"
    )
    fun `Test initial internet connection`(
        hasInternet: Boolean,
        hasValidatedInternet: Boolean,
        expected: Boolean
    ) = runTest {
        val activeNetwork = mockk<Network>()
        val networkCapabilities = mockk<NetworkCapabilities>()

        every { connectivityManager.activeNetwork } returns activeNetwork

        every {
            connectivityManager.getNetworkCapabilities(activeNetwork)
        } returns networkCapabilities

        every {
            networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } returns hasInternet

        every {
            networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        } returns hasValidatedInternet

        connectivity.hasInternetConnection().test {
            val initialState = awaitItem()

            assertThat(initialState).isEqualTo(expected)

            cancelAndIgnoreRemainingEvents()
        }
    }


    @ParameterizedTest
    @CsvSource(
        "false, true, true",
        "true, false, false"
    )
    fun `Test internet connection change`(
        initialHasInternet: Boolean,
        changeInternet: Boolean,
        expected: Boolean
    ) = runTest {
        val activeNetwork = mockk<Network>()
        val networkCapabilities = mockk<NetworkCapabilities>()
        val callbackSlot = slot<ConnectivityManager.NetworkCallback>()

        mockkConstructor(NetworkRequest.Builder::class)
        val networkRequestBuilder = mockk<NetworkRequest.Builder>(relaxed = true)

        every { connectivityManager.activeNetwork } returns activeNetwork

        every {
            connectivityManager.getNetworkCapabilities(activeNetwork)
        } returns networkCapabilities

        every { networkCapabilities.hasCapability(any()) } returns initialHasInternet

        every {
            anyConstructed<NetworkRequest.Builder>().addCapability(any())
        } returns networkRequestBuilder

        justRun {
            connectivityManager.requestNetwork(any(), capture(callbackSlot))
        }

        connectivity.hasInternetConnection().test {
            skipItems(1)

            every { networkCapabilities.hasCapability(any()) } returns changeInternet

            callbackSlot.captured.onCapabilitiesChanged(activeNetwork, networkCapabilities)

            val internetChange = awaitItem()

            assertThat(internetChange).isEqualTo(expected)

            cancelAndIgnoreRemainingEvents()
        }
    }
}