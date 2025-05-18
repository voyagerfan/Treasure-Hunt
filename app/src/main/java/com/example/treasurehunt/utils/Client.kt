package com.example.treasurehunt.utils

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.network.okHttpClient
import com.example.treasurehunt.GetGreetingQuery
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor


object graphQLClient {
    private val apolloClient = ApolloClient.Builder()
        .serverUrl("http://10.0.2.2:4000/graphql")
        .okHttpClient(
            OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build())
        .build()

    suspend fun fetchGreetings(): List<GetGreetingQuery.Greeting?>? {
        val response = apolloClient.query(GetGreetingQuery()).execute()

        Log.d("GraphQL", "Errors: ${response.errors}")
        Log.d("GraphQL", "Data: ${response.data}")
        return response.data?.greetings
    }
}

