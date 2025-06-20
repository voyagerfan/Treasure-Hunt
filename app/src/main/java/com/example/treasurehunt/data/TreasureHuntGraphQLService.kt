package com.example.treasurehunt.data

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.example.treasurehunt.GetGreetingQuery
import com.example.treasurehunt.utils.graphQLClient

class TreasureHuntGraphQLService (private val apolloClient: ApolloClient) {

    suspend fun fetchGreetings(): List<GetGreetingQuery.Greeting?>? {
        val response = apolloClient.query(GetGreetingQuery()).execute()

        Log.d("GraphQL", "Errors: ${response.errors}")
        Log.d("GraphQL", "Data: ${response.data}")
        return response.data?.greetings
    }
}