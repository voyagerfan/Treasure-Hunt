package com.example.treasurehunt.utils

import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.api.Operation

open class Response<T> {
    class Idle<T> : Response<T>()
    data class Loading<T>(val data: T? = null) : Response<T>()
    data class Success<T>(val data: T) : Response<T>()
    data class Error<T>(val exception: Throwable) : Response<T>()
}

fun <T : Operation.Data> ApolloResponse<T>.toResponse(): Response<T> {
    val apolloException = exception
    return when {
        apolloException != null -> Response.Error(apolloException)
        hasErrors() -> Response.Error(Throwable(errors?.firstOrNull()?.message ?: "Unknown GraphQL error"))
        data != null -> Response.Success(data!!)
        else -> Response.Error(Throwable("Unknown error"))
    }
}