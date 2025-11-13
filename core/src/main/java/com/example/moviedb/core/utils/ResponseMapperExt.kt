package com.example.moviedb.core.utils

import com.example.moviedb.core.data.source.remote.response.GenresResponse
import com.example.moviedb.core.data.source.remote.response.MovieDetailResponse
import com.example.moviedb.core.data.source.remote.response.MovieReviewsResponse
import com.example.moviedb.core.data.source.remote.response.MovieVideosResponse
import com.example.moviedb.core.data.source.remote.response.MoviesResponse
import com.example.moviedb.core.domain.model.GenreData
import com.example.moviedb.core.domain.model.MovieData
import com.example.moviedb.core.domain.model.MovieReviewData
import com.example.moviedb.core.domain.model.MovieVideoData

fun List<GenresResponse.GenreData>.mapToGenreData(): List<GenreData> {
    return this.map {
        GenreData(
            id = it.id,
            name = it.name
        )
    }
}

fun List<MoviesResponse.MovieData>.mapToMovieData(): List<MovieData> {
    return this.map {
        MovieData(
            id = it.id,
            title = it.title,
            overview = it.overview,
            posterPath = it.posterPath,
            releaseDate = it.releaseDate
        )
    }
}

fun MovieDetailResponse.mapToMovieData(): MovieData {
    return MovieData(
        id = id,
        title = title,
        overview = overview,
        releaseDate = releaseDate,
        runtime = runtime,
        voteAverage = voteAverage,
        voteCount = voteCount,
        genres = genres.mapToGenreData()
    )
}

fun List<MovieVideosResponse.MovieVideoData>.mapToMovieVideoData(): List<MovieVideoData> {
    return this.map {
        MovieVideoData(
            key = it.key,
            type = it.type
        )
    }
}

fun List<MovieReviewsResponse.MovieReviewData>.mapToMovieReviewData(): List<MovieReviewData> {
    return this.map {
        MovieReviewData(
            id = it.id,
            author = it.author,
            authorDetails = MovieReviewData.AuthorDetails(
                name = it.authorDetails.name,
                username = it.authorDetails.username,
                avatarPath = it.authorDetails.avatarPath,
                rating = it.authorDetails.rating
            ),
            content = it.content,
            createdAt = it.createdAt.formatDateTimeServer()
        )
    }
}