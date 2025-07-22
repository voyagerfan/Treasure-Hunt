package com.example.treasurehunt.data

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.api.Optional
import com.example.treasurehunt.GetGreetingQuery
import com.example.treasurehunt.GetQuestItemsQuery
import com.example.treasurehunt.model.QuestItemQueryParams
import com.example.treasurehunt.type.CoordinateInput
import com.example.treasurehunt.type.RangeInput
import javax.inject.Inject

interface GraphQLApi {
    suspend fun fetchGreetings(): ApolloResponse<GetGreetingQuery.Data>
    suspend fun fetchQuestItems(queryParams: QuestItemQueryParams): ApolloResponse<GetQuestItemsQuery.Data>
}

class TreasureHuntGraphQLService @Inject constructor(private val apolloClient: ApolloClient) : GraphQLApi {
    override suspend fun fetchGreetings(): ApolloResponse<GetGreetingQuery.Data> {
        return apolloClient.query(GetGreetingQuery()).execute()
    }

    override suspend fun fetchQuestItems(
        queryParams: QuestItemQueryParams
    ): ApolloResponse<GetQuestItemsQuery.Data> {
        val query = GetQuestItemsQuery(
            radius = Optional.Present(queryParams.radius),
            rating = Optional.Present(
                RangeInput(min = queryParams.ratingRange!!.first,
                    max = queryParams.ratingRange.second)
            ),
            originCoordinates = Optional.Present(
                CoordinateInput(
                    latitude = queryParams.originCoordinates!!.latitude,
                    longitude = queryParams.originCoordinates.longitude)
            )
        )
        return apolloClient.query(query).execute()
    }
}
