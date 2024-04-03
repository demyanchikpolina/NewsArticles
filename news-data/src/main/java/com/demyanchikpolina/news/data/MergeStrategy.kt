package com.demyanchikpolina.news.data

interface MergeStrategy<E> {

    fun merge(left: E, right: E): E
}

internal class RequestResultMergeStrategy<T : Any> : MergeStrategy<RequestResult<T>> {

    override fun merge(left: RequestResult<T>, right: RequestResult<T>): RequestResult<T> =
        when {
            left is RequestResult.InProgress
                    && right is RequestResult.InProgress -> merge(left, right)

            left is RequestResult.InProgress
                    && right is RequestResult.Success -> merge(left, right)

            left is RequestResult.Success
                    && right is RequestResult.InProgress -> merge(left, right)

            left is RequestResult.Error
                    && right is RequestResult.Success -> merge(left, right)

            else -> TODO()
        }

    private fun merge(
        server: RequestResult.InProgress<T>,
        cache: RequestResult.InProgress<T>
    ): RequestResult<T> = when {
        server.data != null -> RequestResult.InProgress(server.data)
        else -> RequestResult.InProgress(cache.data)
    }

    private fun merge(
        server: RequestResult.InProgress<T>,
        cache: RequestResult.Success<T>
    ): RequestResult<T> = RequestResult.InProgress(cache.data)

    private fun merge(
        server: RequestResult.Success<T>,
        cache: RequestResult.InProgress<T>
    ): RequestResult<T> = RequestResult.InProgress(server.data)

    private fun merge(
        server: RequestResult.Error<T>,
        cache: RequestResult.Success<T>
    ): RequestResult<T> = RequestResult.Error(cache.data, server.error)
}