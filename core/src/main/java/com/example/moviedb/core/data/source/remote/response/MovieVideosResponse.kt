package com.example.moviedb.core.data.source.remote.response

data class MovieVideosResponse(
    val id: Int,
    val results: List<MovieVideoData>
) {
    data class MovieVideoData(
        val key: String,
        val type: String,
    )
}
