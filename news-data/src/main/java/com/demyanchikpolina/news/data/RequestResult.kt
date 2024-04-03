package com.demyanchikpolina.news.data

sealed class RequestResult<out E : Any>(val data: E? = null) {

    class InProgress<E : Any>(data: E? = null) : RequestResult<E>(data)

    class Success<E : Any>(data: E) : RequestResult<E>(data)

    class Error<E : Any>(data: E? = null, val error: Throwable? = null) : RequestResult<E>(data)
}

fun <I : Any, O : Any> RequestResult<I>.map(mapper: (I) -> O): RequestResult<O> {
    return when(this) {
        is RequestResult.Error -> RequestResult.Error(data?.let(mapper))
        is RequestResult.InProgress -> RequestResult.InProgress(data?.let(mapper))
        is RequestResult.Success -> {
            val outData = mapper(checkNotNull(data))
            RequestResult.Success(outData)
        }
    }
}

internal fun <T : Any> Result<T>.toRequestResult() : RequestResult<T> =
    when {
        isSuccess -> RequestResult.Success(getOrThrow())
        isFailure -> RequestResult.Error()
        else -> error("Impossible branch")
    }