package com.example.moviedb.core.domain.usecase

import com.example.moviedb.core.data.source.Resource
import com.example.moviedb.core.domain.model.GenreData
import com.example.moviedb.core.domain.repository.IMovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetGenreList(private val repository: IMovieRepository) {
    private var genreList: List<GenreData>? = null

    operator fun invoke(): Flow<Resource<List<GenreData>>> = flow {
        if (!genreList.isNullOrEmpty()) {
            emit(Resource.Success(genreList.orEmpty()))
            return@flow
        }

        repository.getGenres().collect { resource ->
            if (resource is Resource.Success) {
                genreList = resource.data
            }

            emit(resource)
        }
    }
}