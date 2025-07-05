package com.example.treasurehunt

import com.apollographql.apollo.api.ApolloResponse
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
import org.junit.Before
import org.junit.Test
import java.util.UUID
import kotlin.test.assertContentEquals

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
        val greetingsList = listOf(
            GetGreetingQuery.Greeting(title = "Hello", subtitle = "World")
        )
        val myQueryData = GetGreetingQuery.Data(greetings = greetingsList)
        val mockApi = mockk<GraphQLApi>()
        val successResponse = ApolloResponse.Builder(GetGreetingQuery(), UUID.randomUUID())
            .data(myQueryData)
            .build()
        coEvery { mockApi.fetchGreetings() } returns successResponse

        val viewModel = TreasureViewModel(
            applicationContext = mockk(relaxed = true),
            fusedLocationClient = mockk(relaxed = true),
            locationRequest = mockk(relaxed = true),
            apolloClient = mockApi
        )

        viewModel.getGreetings()
        testScheduler.advanceUntilIdle()
        assertContentEquals(successResponse.data?.greetings, viewModel.gameStartScreenState.value.greetings)
    }
}