package com.example.moviedb.core.data.source

sealed class Resource<T>(val data: T? = null, val errorCode: Int? = null, val message: String? = null) {
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(errorCode: Int, message: String, data: T? = null) : Resource<T>(data, errorCode, message)
}
