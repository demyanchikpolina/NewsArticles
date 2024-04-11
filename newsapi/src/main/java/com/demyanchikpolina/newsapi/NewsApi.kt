package com.demyanchikpolina.newsapi

import androidx.annotation.IntRange
import com.demyanchikpolina.newsapi.models.ArticleDTO
import com.demyanchikpolina.newsapi.models.Language
import com.demyanchikpolina.newsapi.models.ResponseDTO
import com.demyanchikpolina.newsapi.models.SortBy
import com.demyanchikpolina.newsapi.utils.NewsApiKeyInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.skydoves.retrofit.adapters.result.ResultCallAdapterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Date

interface NewsApi {

    @GET("everything")
    suspend fun everything(
        @Query("q") query: String = "",
        @Query("from") from: Date? = null,
        @Query("to") to: Date? = null,
        @Query("language") languages: List<@JvmSuppressWildcards Language>? = null,
        @Query("sortBy") sortBy: SortBy = SortBy.PUBLISHED_AT,
        @Query("pageSize") @IntRange(from = 0, to = 100) pageSize: Int = 100,
        @Query("page") @IntRange(from = 1) page: Int = 1,
    ): Result<ResponseDTO<ArticleDTO>>
}
fun NewsApi(
        baseUrl: String,
        apiKey: String,
        okHttpClient: OkHttpClient? = null,
        json: Json = Json
    ) : NewsApi = retrofit(baseUrl, apiKey, okHttpClient, json).create()

    private fun retrofit(
        baseUrl: String,
        apiKey: String,
        okHttpClient: OkHttpClient?,
        json: Json = Json
    ): Retrofit {
        val jsonConverterFactory = json.asConverterFactory("application/json".toMediaType())

        val client = (okHttpClient?.newBuilder() ?: OkHttpClient.Builder())
            .addInterceptor(NewsApiKeyInterceptor(apiKey))
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(jsonConverterFactory)
            .addCallAdapterFactory(ResultCallAdapterFactory.create())
            .build()
    }
