package com.example.treasurehunt

import com.example.treasurehunt.data.GraphQLApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import io.mockk.*

class GraphQLGreetingTest {
    @Test
    fun loadedGreetings_match_expected_values() = runTest {
        val mockApi = mockk<GraphQLApi>()


    }
}