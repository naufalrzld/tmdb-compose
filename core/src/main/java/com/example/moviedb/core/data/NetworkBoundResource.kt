package com.example.moviedb.core.data

import com.example.moviedb.core.data.source.Resource
import com.example.moviedb.core.data.source.remote.network.ApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

abstract class NetworkBoundResource<ResultType, RequestType> {

    private var result: Flow<Resource<ResultType>> = flow {
        emit(Resource.Loading())
        when (val response = createCall()) {
            is ApiResponse.Success -> emit(Resource.Success(callResult(response.data)))
            is ApiResponse.Error -> emit(Resource.Error(response.errorCode, response.errorMessage))
        }
    }

    abstract suspend fun createCall(): ApiResponse<RequestType>

    abstract suspend fun callResult(response: RequestType): ResultType

    fun asFlow(): Flow<Resource<ResultType>> = result
}
