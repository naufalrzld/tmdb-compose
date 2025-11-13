package com.example.moviedb.core.domain.repository

import androidx.paging.PagingData
import com.example.moviedb.core.data.source.Resource
import com.example.moviedb.core.domain.model.GenreData
import com.example.moviedb.core.domain.model.MovieData
import com.example.moviedb.core.domain.model.MovieReviewData
import com.example.moviedb.core.domain.model.MovieVideoData
import kotlinx.coroutines.flow.Flow

interface IMovieRepository {
    fun getGenres(): Flow<Resource<List<GenreData>>>

    fun getMovies(withGenres: String? = null): Flow<PagingData<MovieData>>

    fun getMovieDetail(movieId: Int): Flow<Resource<MovieData>>

    fun getMovieVideos(movieId: Int): Flow<Resource<List<MovieVideoData>>>

    fun getMovieReviews(movieId: Int): Flow<PagingData<MovieReviewData>>
}