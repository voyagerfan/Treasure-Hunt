package com.example.treasurehunt.data

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.api.Optional
import com.example.treasurehunt.GetGreetingQuery
import com.example.treasurehunt.GetQuestItemsQuery
import com.example.treasurehunt.model.Coordinate
import com.example.treasurehunt.type.CoordinateInput
import com.example.treasurehunt.type.RangeInput
import javax.inject.Inject

interface GraphQLApi {
    suspend fun fetchGreetings(): ApolloResponse<GetGreetingQuery.Data>
    suspend fun fetchQuestItems(
        rating: Pair<Double, Double>,
        radius: Double,
        coordinate: Coordinate
    ): ApolloResponse<GetQuestItemsQuery.Data>
}

class TreasureHuntGraphQLService @Inject constructor(private val apolloClient: ApolloClient) : GraphQLApi {
    override suspend fun fetchGreetings(): ApolloResponse<GetGreetingQuery.Data> {
        return apolloClient.query(GetGreetingQuery()).execute()
    }

    override suspend fun fetchQuestItems(
        rating: Pair<Double, Double>,
        radius: Double,
        coordinate: Coordinate
    ): ApolloResponse<GetQuestItemsQuery.Data> {
        val query = GetQuestItemsQuery(
            rating = Optional.Present(RangeInput(min = rating.first, max = rating.second)),
            radius = Optional.Present(radius),
            originCoordinates = Optional.Present(CoordinateInput(latitude = coordinate.latitude, longitude = coordinate.longitude ))
        )
        return apolloClient.query(query).execute()
    }
}
