package com.example.moviedb.core.data.source.remote.response

data class GenresResponse(
    val genres: List<GenreData>
) {
    data class GenreData(
        val id: Int,
        val name: String
    )
}
