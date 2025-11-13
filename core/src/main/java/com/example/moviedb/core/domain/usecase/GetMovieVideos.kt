package com.example.moviedb.core.domain.usecase

import com.example.moviedb.core.data.source.Resource
import com.example.moviedb.core.domain.model.MovieVideoData
import com.example.moviedb.core.domain.repository.IMovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetMovieVideos(private val repository: IMovieRepository) {

    operator fun invoke(movieId: Int): Flow<Resource<List<MovieVideoData>>> = flow {
        repository.getMovieVideos(movieId).collect { resource ->
            if (resource is Resource.Success) {
                val data = resource.data.orEmpty()
                emit(
                    Resource.Success(
                        data.filter { it.type.equals("Trailer", ignoreCase = true) }
                    )
                )
            } else {
                emit(resource)
            }
        }
    }
}