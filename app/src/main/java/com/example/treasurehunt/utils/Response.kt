package com.example.treasurehunt.utils

open class Response<T> {
    data class Loading<T>(val data: T? = null) : Response<T>()
    data class Success<T>(val data: T) : Response<T>()
    data class Error(val exception: Throwable) : Response<Nothing>()
}