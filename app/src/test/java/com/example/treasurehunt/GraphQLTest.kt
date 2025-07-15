package com.example.treasurehunt

import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.exception.ApolloException
import com.example.treasurehunt.data.GraphQLApi
import com.example.treasurehunt.utils.Response
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.UUID
import kotlin.test.assertContentEquals

@OptIn(ExperimentalCoroutinesApi::class)
class GraphQLGreetingTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var greetingsList: List<GetGreetingQuery.Greeting>
    private lateinit var myQueryData: GetGreetingQuery.Data
    private lateinit var mockApi: GraphQLApi
    private lateinit var viewModel: TreasureViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        greetingsList = listOf(
            GetGreetingQuery.Greeting(title = "Hello", subtitle = "World")
        )
        myQueryData = GetGreetingQuery.Data(greetings = greetingsList)
        mockApi = mockk<GraphQLApi>()
        viewModel = TreasureViewModel(
            applicationContext = mockk(relaxed = true),
            fusedLocationClient = mockk(relaxed = true),
            locationRequest = mockk(relaxed = true),
            apolloClient = mockApi
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getGreetings_withValidResponse_updatesStateCorrectly() = runTest {
        /**
         * Given a valid server response, this test verifies that the viewModel
         * state is updated to success with the correct list.
         */

        val successResponse = ApolloResponse.Builder(GetGreetingQuery(), UUID.randomUUID())
            .data(myQueryData)
            .build()
        coEvery { mockApi.fetchGreetings() } returns successResponse
        viewModel.getGreetings()
        advanceUntilIdle()

        val state = viewModel.gameStartScreenState.value.greetings
        require(state is Response.Success)
        assertContentEquals(greetingsList, state.data.greetings)
    }

    @Test
    fun getGreetings_withException_isHandled() = runTest {
        /**
         * Given a exception from the server, this test verifies that the viewModel
         * state updates to the correct response type
         */
        val mockException = mockk<ApolloException>(relaxed = true)
        val exceptionResponse = ApolloResponse.Builder(GetGreetingQuery(), UUID.randomUUID())
            .exception(exception = mockException)
            .build()
        coEvery { mockApi.fetchGreetings() } returns exceptionResponse
        viewModel.getGreetings()
        testScheduler.advanceUntilIdle()
        val state = viewModel.gameStartScreenState.value.greetings
        require(state is Response.Error)
        assertEquals(mockException, state.exception)
    }
}