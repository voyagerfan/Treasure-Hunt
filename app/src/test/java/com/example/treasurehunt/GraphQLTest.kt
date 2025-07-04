package com.example.treasurehunt

import com.example.treasurehunt.data.GraphQLApi
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GraphQLGreetingTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getGreetings_withValidResponse_updatesStateCorrectly() = runTest {
        /**
         * Given a valid server response, this test verifies that the viewModel
         * state is updated.
         */
        val mockApi = mockk<GraphQLApi>()
        val mockGreeting = listOf(GetGreetingQuery.Greeting(
                title = "Hello",
                subtitle = "World"
        ))
        coEvery { mockApi.fetchGreetings() } returns mockGreeting

        val viewModel = TreasureViewModel(
            applicationContext = mockk(relaxed = true),
            fusedLocationClient = mockk(relaxed = true),
            locationRequest = mockk(relaxed = true),
            apolloClient = mockApi
        )

        viewModel.getGreetings()
        testScheduler.advanceUntilIdle()
        assertEquals(mockGreeting, viewModel.gameStartScreenState.value.greetings)
    }
}