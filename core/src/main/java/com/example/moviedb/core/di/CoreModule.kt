package com.example.moviedb.core.di

import com.example.moviedb.core.data.repository.MovieRepository
import com.example.moviedb.core.data.source.remote.RemoteDataSource
import com.example.moviedb.core.data.source.remote.RetrofitClient
import com.example.moviedb.core.data.source.remote.network.ApiServices
import com.example.moviedb.core.domain.repository.IMovieRepository
import com.example.moviedb.core.domain.usecase.GetGenreList
import com.example.moviedb.core.domain.usecase.GetMovieVideos
import org.koin.dsl.module

val coreModule = module {
    single { RetrofitClient() }
    single { provideApiServices(get()) }
    single { RemoteDataSource(get()) }
    single<IMovieRepository> { MovieRepository(get()) }
    factory { GetGenreList(get()) }
    factory { GetMovieVideos(get()) }
}

private fun provideApiServices(
    retrofitClient: RetrofitClient
): ApiServices = retrofitClient.createApiServices()