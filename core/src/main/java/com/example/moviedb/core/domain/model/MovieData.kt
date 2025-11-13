package com.example.moviedb.core.domain.model

data class MovieData(
    val id: Int,
    val title: String,
    val overview: String,
    val releaseDate: String,
    val runtime: Int? = null,
    val voteAverage: Double? = null,
    val voteCount: Int? = null,
    val posterPath: String? = null,
    val genres: List<GenreData>? = null
)
