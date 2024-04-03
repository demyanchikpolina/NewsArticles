package com.demyanchikpolina.newsapi.utils

import okhttp3.Interceptor
import okhttp3.Response

// 184a80217bca4d1aa8b4061a00a64798
internal class NewsApiKeyInterceptor(private val apiKey: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain
                .request()
                .newBuilder()
                .addHeader("X-Api-Key", apiKey)
                .build()
        )
    }
}