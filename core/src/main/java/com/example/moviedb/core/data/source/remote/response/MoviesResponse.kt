package com.example.moviedb.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class MoviesResponse(
    val page: Int,
    val results: List<MovieData>,

    @SerializedName("total_pages")
    val totalPages: Int,

    @SerializedName("total_results")
    val totalResults: Int
) {
    data class MovieData(
        val id: Int,
        val title: String,
        val overview: String,

        @SerializedName("poster_path")
        val posterPath: String?,

        @SerializedName("release_date")
        val releaseDate: String
    )
}
