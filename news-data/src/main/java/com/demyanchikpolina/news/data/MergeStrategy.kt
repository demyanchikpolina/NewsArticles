package com.demyanchikpolina.news.data

import com.demyanchikpolina.news.data.RequestResult.Error
import com.demyanchikpolina.news.data.RequestResult.InProgress
import com.demyanchikpolina.news.data.RequestResult.Success

interface MergeStrategy<E> {

    fun merge(left: E, right: E): E
}

internal class RequestResultMergeStrategy<T : Any> : MergeStrategy<RequestResult<T>> {

    override fun merge(left: RequestResult<T>, right: RequestResult<T>): RequestResult<T> =
        when {
            left is InProgress && right is InProgress -> merge(left, right)

            left is InProgress && right is Success -> merge(left, right)

            left is InProgress && right is Error -> merge(left, right)

            left is Success && right is Success -> merge(left, right)

            left is Success && right is InProgress -> merge(left, right)

            left is Success && right is Error -> merge(left, right)

            left is Error && right is Success -> merge(left, right)

            left is Error && right is InProgress -> merge(left, right)

            left is Error && right is Error -> merge(left, right)

            else -> error("Unimplemented branch left=$left & right=$right")
        }

    private fun merge(server: InProgress<T>, cache: InProgress<T>): RequestResult<T> =
        when {
            server.data != null -> InProgress(server.data)
            else -> InProgress(cache.data)
        }

    @Suppress("UNUSED_PARAMETER")
    private fun merge(server: InProgress<T>, cache: Success<T>): RequestResult<T> =
        InProgress(cache.data)

    @Suppress("UNUSED_PARAMETER")
    private fun merge(server: InProgress<T>, cache: Error<T>): RequestResult<T> =
        server

    @Suppress("UNUSED_PARAMETER")
    private fun merge(server: Success<T>, cache: InProgress<T>): RequestResult<T> =
        InProgress(server.data)

    @Suppress("UNUSED_PARAMETER")
    private fun merge(server: Success<T>, cache: Success<T>): RequestResult<T> =
        Success(server.data)

    @Suppress("UNUSED_PARAMETER")
    private fun merge(server: Success<T>, cache: Error<T>): RequestResult<T> =
        server

    private fun merge(server: Error<T>, cache: Success<T>): RequestResult<T> =
        Error(cache.data, server.error)

    private fun merge(server: Error<T>, cache: InProgress<T>): RequestResult<T> =
        Error(data = server.data ?: cache.data, error = server.error)

    private fun merge(server: Error<T>, cache: Error<T>): RequestResult<T> =
        Error(data = server.data ?: cache.data, error = server.error ?: cache.error)
}
