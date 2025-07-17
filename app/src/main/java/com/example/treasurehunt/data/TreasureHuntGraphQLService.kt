package com.example.treasurehunt.data

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.ApolloResponse
import com.example.treasurehunt.GetGreetingQuery
import javax.inject.Inject

interface GraphQLApi {
    suspend fun fetchGreetings(): ApolloResponse<GetGreetingQuery.Data>
}

class TreasureHuntGraphQLService @Inject constructor(private val apolloClient: ApolloClient) : GraphQLApi {
    override suspend fun fetchGreetings(): ApolloResponse<GetGreetingQuery.Data> {
        return apolloClient.query(GetGreetingQuery()).execute()
    }
}
