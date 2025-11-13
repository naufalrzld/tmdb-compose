package com.example.moviedb.core.domain.model

data class GenreData(
    val id: Int,
    val name: String,
    val isSelected: Boolean = false
)
