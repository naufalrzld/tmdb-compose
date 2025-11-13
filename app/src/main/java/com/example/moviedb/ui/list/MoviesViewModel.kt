package com.example.moviedb.ui.list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.moviedb.core.data.source.Resource
import com.example.moviedb.core.domain.model.GenreData
import com.example.moviedb.core.domain.model.MovieData
import com.example.moviedb.core.domain.repository.IMovieRepository
import com.example.moviedb.core.domain.usecase.GetGenreList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MoviesViewModel(
    private val getGenreList: GetGenreList,
    private val movieRepository: IMovieRepository
) : ViewModel() {

    private val _isGenreLoading = mutableStateOf(false)
    val isGenreLoading: State<Boolean> = _isGenreLoading

    private val _genresState = mutableStateOf(emptyList<GenreData>())
    val genresState: State<List<GenreData>> = _genresState

    private val _selectedGenresState = mutableStateOf(emptyList<GenreData>())
    val selectedGenresState: State<List<GenreData>> = _selectedGenresState

    private val _movies = MutableStateFlow<PagingData<MovieData>>(PagingData.empty())
    val movies: StateFlow<PagingData<MovieData>> = _movies

    private val _onError = MutableSharedFlow<String>()
    val onError: SharedFlow<String> = _onError


    fun getGenres() {
        viewModelScope.launch {
            getGenreList().collect { resource ->
                _isGenreLoading.value = resource is Resource.Loading

                when (resource) {
                    is Resource.Loading -> Unit
                    is Resource.Success -> {
                        val currentSelectedGenre = _genresState.value
                            .filter { it.isSelected }
                            .map { it.id }

                        _genresState.value = resource.data.orEmpty().map { genre ->
                            genre.copy(isSelected = currentSelectedGenre.contains(genre.id))
                        }
                    }
                    is Resource.Error -> _onError.emit(resource.message ?: "Unknown error occurred")
                }
            }
        }
    }

    fun setSelectedGenres(genreData: GenreData) {
        _genresState.value = _genresState.value.map {
            if (it.id == genreData.id) {
                it.copy(isSelected = !it.isSelected)
            } else {
                it
            }
        }

        _selectedGenresState.value = _genresState.value.filter { it.isSelected }
    }

    fun getMovies(withGenres: String? = null) {
        viewModelScope.launch {
            _movies.value = PagingData.empty()

            movieRepository.getMovies(withGenres).cachedIn(viewModelScope).collectLatest {
                _movies.value = it
            }
        }
    }
}