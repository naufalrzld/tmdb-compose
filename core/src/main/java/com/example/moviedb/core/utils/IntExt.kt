package com.example.moviedb.core.utils

fun Int.formatToMovieDuration(): String {
    val hour = this / 60
    val minutes = this % 60

    return if (hour > 0) {
        String.format("%dh %dm", hour, minutes)
    } else {
        String.format("0h %dm", minutes)
    }
}