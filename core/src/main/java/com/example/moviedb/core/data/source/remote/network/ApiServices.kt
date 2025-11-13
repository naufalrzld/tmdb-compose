package com.example.moviedb.core.data.source.remote.network

import com.example.moviedb.core.data.source.remote.response.GenresResponse
import com.example.moviedb.core.data.source.remote.response.MovieDetailResponse
import com.example.moviedb.core.data.source.remote.response.MovieReviewsResponse
import com.example.moviedb.core.data.source.remote.response.MovieVideosResponse
import com.example.moviedb.core.data.source.remote.response.MoviesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {
    @GET("genre/movie/list")
    suspend fun getGenres(): GenresResponse

    @GET("discover/movie")
    suspend fun getMovies(
        @Query("page") page: Int,
        @Query("with_genres") withGenres: String?
    ): MoviesResponse

    @GET("movie/{id}")
    suspend fun getMovieDetail(
        @Path("id") id: Int
    ): MovieDetailResponse

    @GET("movie/{id}/videos")
    suspend fun getMovieVideos(
        @Path("id") id: Int
    ): MovieVideosResponse

    @GET("movie/{id}/reviews")
    suspend fun getMovieReviews(
        @Path("id") id: Int,
        @Query("page") page: Int
    ): MovieReviewsResponse
}