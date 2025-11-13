package com.example.moviedb.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class MovieReviewsResponse(
    val id: Int,
    val page: Int,
    val results: List<MovieReviewData>,

    @SerializedName("total_pages")
    val totalPages: Int,

    @SerializedName("total_results")
    val totalResults: Int
) {
    data class MovieReviewData(
        val id: String,
        val author: String,

        @SerializedName("author_details")
        val authorDetails: AuthorDetails,

        @SerializedName("created_at")
        val createdAt: String,
        val content: String
    ) {
        data class AuthorDetails(
            val name: String,
            val username: String,

            @SerializedName("avatar_path")
            val avatarPath: String?,

            val rating: Int
        )
    }
}
