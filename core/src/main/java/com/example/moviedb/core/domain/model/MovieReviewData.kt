package com.example.moviedb.core.domain.model

data class MovieReviewData(
    val id: String,
    val author: String,
    val authorDetails: AuthorDetails,
    val content: String,
    val createdAt: String
) {
    data class AuthorDetails(
        val name: String,
        val username: String,
        val avatarPath: String?,
        val rating: Int?
    )
}
