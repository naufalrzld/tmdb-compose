package com.example.moviedb.core.utils

import com.google.gson.JsonParser
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

object ResponseErrorMapper {
    fun mapResponseError(error: Throwable): Pair<Int, String> {
        var message = "Server error"
        var code = 0
        message = when (error) {
            is HttpException -> {
                code = error.code()
                try {
                    val errorJsonString = error.response()?.errorBody()?.string()
                    JsonParser().parse(errorJsonString)
                        .asJsonObject["status_message"]
                        .asString
                } catch (e: Exception) {
                    message
                }
            }
            is UnknownHostException -> {
                code = 0
                "No internet connection"
            }
            is SSLException -> {
                code = 500
                "SSL Error"
            }
            else -> {
                code = 500
                error.message ?: message
            }
        }
        return Pair(code, message)
    }
}