package com.example.moviedb.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.moviedb.core.data.NetworkBoundResource
import com.example.moviedb.core.data.source.Resource
import com.example.moviedb.core.data.source.remote.RemoteDataSource
import com.example.moviedb.core.data.source.remote.network.ApiResponse
import com.example.moviedb.core.data.source.remote.response.GenresResponse
import com.example.moviedb.core.data.source.remote.response.MovieDetailResponse
import com.example.moviedb.core.data.source.remote.response.MovieVideosResponse
import com.example.moviedb.core.domain.model.GenreData
import com.example.moviedb.core.domain.model.MovieData
import com.example.moviedb.core.domain.model.MovieReviewData
import com.example.moviedb.core.domain.model.MovieVideoData
import com.example.moviedb.core.domain.repository.IMovieRepository
import com.example.moviedb.core.utils.mapToGenreData
import com.example.moviedb.core.utils.mapToMovieData
import com.example.moviedb.core.utils.mapToMovieReviewData
import com.example.moviedb.core.utils.mapToMovieVideoData
import kotlinx.coroutines.flow.Flow
import okio.IOException
import retrofit2.HttpException

class MovieRepository(private val remoteDataSource: RemoteDataSource) : IMovieRepository {
    override fun getGenres(): Flow<Resource<List<GenreData>>> {
        return object : NetworkBoundResource<List<GenreData>, List<GenresResponse.GenreData>>() {
            override suspend fun createCall(): ApiResponse<List<GenresResponse.GenreData>> {
                return remoteDataSource.getGenres()
            }

            override suspend fun callResult(response: List<GenresResponse.GenreData>): List<GenreData> {
                return response.mapToGenreData()
            }

        }.asFlow()
    }

    override fun getMovies(withGenres: String?): Flow<PagingData<MovieData>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                object : PagingSource<Int, MovieData>() {
                    override fun getRefreshKey(state: PagingState<Int, MovieData>): Int? {
                        return state.anchorPosition?.let { anchorPosition ->
                            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
                        }
                    }

                    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieData> {
                        return try {
                            val currentPage = params.key ?: 1
                            when (val response = remoteDataSource.getMovies(page = currentPage, withGenres = withGenres)) {
                                is ApiResponse.Success -> {
                                    val data = response.data
                                    val nextKey = if (data.isEmpty()) null else currentPage + 1
                                    LoadResult.Page(
                                        data = data.mapToMovieData(),
                                        prevKey = if (currentPage == 1) null else currentPage - 1,
                                        nextKey = nextKey
                                    )
                                }
                                is ApiResponse.Error -> {
                                    LoadResult.Error(Exception(response.errorMessage))
                                }
                            }
                        } catch (e: IOException) {
                            LoadResult.Error(e)
                        } catch (exception: HttpException) {
                            LoadResult.Error(exception)
                        }
                    }
                }
            }
        ).flow
    }

    override fun getMovieDetail(movieId: Int): Flow<Resource<MovieData>> {
        return object : NetworkBoundResource<MovieData, MovieDetailResponse>() {
            override suspend fun createCall(): ApiResponse<MovieDetailResponse> {
                return remoteDataSource.getMovieDetail(movieId)
            }

            override suspend fun callResult(response: MovieDetailResponse): MovieData {
                return response.mapToMovieData()
            }

        }.asFlow()
    }

    override fun getMovieVideos(movieId: Int): Flow<Resource<List<MovieVideoData>>> {
        return object : NetworkBoundResource<List<MovieVideoData>, List<MovieVideosResponse.MovieVideoData>>() {
            override suspend fun createCall(): ApiResponse<List<MovieVideosResponse.MovieVideoData>> {
                return remoteDataSource.getMovieVideos(movieId)
            }

            override suspend fun callResult(response: List<MovieVideosResponse.MovieVideoData>): List<MovieVideoData> {
                return response.mapToMovieVideoData()
            }

        }.asFlow()
    }

    override fun getMovieReviews(movieId: Int): Flow<PagingData<MovieReviewData>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                object : PagingSource<Int, MovieReviewData>() {
                    override fun getRefreshKey(state: PagingState<Int, MovieReviewData>): Int? {
                        return state.anchorPosition?.let { anchorPosition ->
                            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
                        }
                    }

                    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieReviewData> {
                        return try {
                            val currentPage = params.key ?: 1
                            when (val response = remoteDataSource.getMovieReviews(movieId = movieId, page = currentPage)) {
                                is ApiResponse.Success -> {
                                    val data = response.data
                                    val nextKey = if (data.isEmpty()) null else currentPage + 1
                                    LoadResult.Page(
                                        data = data.mapToMovieReviewData(),
                                        prevKey = if (currentPage == 1) null else currentPage - 1,
                                        nextKey = nextKey
                                    )
                                }
                                is ApiResponse.Error -> {
                                    LoadResult.Error(Exception(response.errorMessage))
                                }
                            }
                        } catch (e: IOException) {
                            LoadResult.Error(e)
                        } catch (exception: HttpException) {
                            LoadResult.Error(exception)
                        }
                    }
                }
            }
        ).flow
    }
}