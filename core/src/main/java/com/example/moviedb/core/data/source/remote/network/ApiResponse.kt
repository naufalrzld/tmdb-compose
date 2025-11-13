package com.example.moviedb.core.data.source.remote.network

sealed class ApiResponse<out T> {
    data class Success<out T>(val data: T) : ApiResponse<T>()
    data class Error(val errorCode: Int, val errorMessage: String) : ApiResponse<Nothing>()
}