package com.example.moviedb.ui.detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.moviedb.core.data.source.Resource
import com.example.moviedb.core.domain.model.MovieData
import com.example.moviedb.core.domain.model.MovieReviewData
import com.example.moviedb.core.domain.repository.IMovieRepository
import com.example.moviedb.core.domain.usecase.GetMovieVideos
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val repository: IMovieRepository,
    private val movieVideos: GetMovieVideos
) : ViewModel() {

    private val _isVideoLoading = mutableStateOf(false)
    val isVideoLoading: State<Boolean> = _isVideoLoading


    private val _movieState = mutableStateOf<MovieData?>(null)
    val movieState: State<MovieData?> = _movieState

    private val _videoId = mutableStateOf<String?>(null)
    val videoId: State<String?> = _videoId

    private val _reviews = MutableStateFlow<PagingData<MovieReviewData>>(PagingData.empty())
    val reviews: StateFlow<PagingData<MovieReviewData>> = _reviews

    private val _onError = MutableSharedFlow<String>()
    val onError: SharedFlow<String> = _onError

    fun getMovieDetail(movieId: Int) {
        viewModelScope.launch {
            repository.getMovieDetail(movieId).collect { resource ->
                when (resource) {
                    is Resource.Loading -> Unit
                    is Resource.Success -> _movieState.value = resource.data
                    is Resource.Error -> _onError.emit(resource.message ?: "Unknown Error occurred")
                }
            }
        }
    }

    fun getMovieVideos(movieId: Int) {
        viewModelScope.launch {
            movieVideos(movieId).collect { resource ->
                _isVideoLoading.value = resource is Resource.Loading
                when (resource) {
                    is Resource.Loading -> Unit
                    is Resource.Success -> resource.data?.let { data ->
                        _videoId.value = if (data.isNotEmpty()) data.random().key else null
                    }
                    is Resource.Error -> _onError.emit(resource.message ?: "Unknown Error occurred")
                }
            }
        }
    }

    fun getMovieReviews(movieId: Int) {
        viewModelScope.launch {
            repository.getMovieReviews(movieId).cachedIn(viewModelScope).collectLatest {
                _reviews.value = it
            }
        }
    }
}