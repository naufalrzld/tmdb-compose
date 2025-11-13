package com.example.moviedb.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class MovieDetailResponse(
    val id: Int,
    val title: String,
    val genres: List<GenresResponse.GenreData>,
    val overview: String,
    val runtime: Int,

    @SerializedName("release_date")
    val releaseDate: String,

    @SerializedName("vote_average")
    val voteAverage: Double,

    @SerializedName("vote_count")
    val voteCount: Int
)