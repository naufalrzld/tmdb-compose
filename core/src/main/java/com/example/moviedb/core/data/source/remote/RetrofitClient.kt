package com.example.moviedb.core.data.source.remote

import com.example.moviedb.core.BuildConfig
import com.example.moviedb.core.data.source.remote.network.ApiServices
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient {

    fun createApiServices(): ApiServices {
        val client = provideOkHttpClient()
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.TMDB_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiServices::class.java)

    }

    private fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val authInterceptor = provideAuthInterceptor()

        return OkHttpClient().newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()
    }

    private fun provideAuthInterceptor(): Interceptor {
        return Interceptor { chain ->
            val req = chain.request()
            val reqBuilder = req.newBuilder().apply {
                addHeader("Content-Type", "application/json")
                if (req.headers["Authorization"].isNullOrEmpty()) {
                    addHeader("Authorization", "Bearer ${BuildConfig.TMDB_AUTH}")
                }
            }.build()
            chain.proceed(reqBuilder)
        }
    }
}