package com.example.moviedb.core.data.source.remote

import com.example.moviedb.core.data.source.remote.network.ApiResponse
import com.example.moviedb.core.data.source.remote.network.ApiServices
import com.example.moviedb.core.data.source.remote.response.GenresResponse
import com.example.moviedb.core.data.source.remote.response.MovieDetailResponse
import com.example.moviedb.core.data.source.remote.response.MovieReviewsResponse
import com.example.moviedb.core.data.source.remote.response.MovieVideosResponse
import com.example.moviedb.core.data.source.remote.response.MoviesResponse
import com.example.moviedb.core.utils.ResponseErrorMapper

class RemoteDataSource(private val apiServices: ApiServices) {
    suspend fun getGenres(): ApiResponse<List<GenresResponse.GenreData>> {
        return try {
            val result = apiServices.getGenres()
            ApiResponse.Success(result.genres)
        } catch (t: Throwable) {
            val (code, message) = ResponseErrorMapper.mapResponseError(t)
            ApiResponse.Error(code, message)
        }
    }

    suspend fun getMovies(
        page: Int,
        withGenres: String? = null,
    ): ApiResponse<List<MoviesResponse.MovieData>> {
        return try {
            val result = apiServices.getMovies(page, withGenres)
            ApiResponse.Success(result.results)
        } catch (t: Throwable) {
            val (code, message) = ResponseErrorMapper.mapResponseError(t)
            ApiResponse.Error(code, message)
        }
    }

    suspend fun getMovieDetail(movieId: Int): ApiResponse<MovieDetailResponse> {
        return try {
            val result = apiServices.getMovieDetail(movieId)
            ApiResponse.Success(result)
        } catch (t: Throwable) {
            val (code, message) = ResponseErrorMapper.mapResponseError(t)
            ApiResponse.Error(code, message)
        }
    }

    suspend fun getMovieVideos(movieId: Int): ApiResponse<List<MovieVideosResponse.MovieVideoData>> {
        return try {
            val result = apiServices.getMovieVideos(movieId)
            ApiResponse.Success(result.results)
        } catch (t: Throwable) {
            val (code, message) = ResponseErrorMapper.mapResponseError(t)
            ApiResponse.Error(code, message)
        }
    }

    suspend fun getMovieReviews(movieId: Int, page: Int): ApiResponse<List<MovieReviewsResponse.MovieReviewData>> {
        return try {
            val result = apiServices.getMovieReviews(movieId, page)
            ApiResponse.Success(result.results)
        } catch (t: Throwable) {
            val (code, message) = ResponseErrorMapper.mapResponseError(t)
            ApiResponse.Error(code, message)
        }
    }
}