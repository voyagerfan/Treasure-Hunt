package com.example.treasurehunt

import android.content.Context
import com.example.treasurehunt.data.GraphQLApi
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.test.runTest
import org.junit.Test
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Assert.assertEquals

class GraphQLGreetingTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun loadedGreetings_match_expected_values() = runTest {
        val mockApi = mockk<GraphQLApi>()
        val mockGreeting = mockk<GetGreetingQuery.Greeting>()
        val mockGreetings = listOf(mockGreeting)
        coEvery { mockApi.fetchGreetings() } returns mockGreetings

        val viewModel = TreasureViewModel(
            applicationContext = mockk<Context>(),
            fusedLocationClient = mockk<FusedLocationProviderClient>(),
            locationRequest = mockk<CurrentLocationRequest>(),
            apolloClient = mockApi
        )

        viewModel.getGreetings()
        advanceUntilIdle()

        assertEquals(mockGreetings, viewModel.gameStartScreenState.value.greetings)
    }
}