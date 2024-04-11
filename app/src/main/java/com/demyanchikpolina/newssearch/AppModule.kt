package com.demyanchikpolina.newssearch

import android.content.Context
import com.demyanchikpolina.database.NewsDatabase
import com.demyanchikpolina.news.common.AndroidLogcatLogger
import com.demyanchikpolina.news.common.AppDispatchers
import com.demyanchikpolina.news.common.Logger
import com.demyanchikpolina.newsapi.NewsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient? {
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
                .apply { setLevel(HttpLoggingInterceptor.Level.BODY) }

            return OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
        }
        return null
    }

    @Provides
    @Singleton
    fun provideNewsApi(okHttpClient: OkHttpClient?): NewsApi {
        return NewsApi(
            baseUrl = BuildConfig.NEWS_API_BASE_URL,
            apiKey = BuildConfig.NEWS_API_KEY,
            okHttpClient = okHttpClient,
            json = Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            }
        )
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NewsDatabase {
        return NewsDatabase(context)
    }

    @Provides
    @Singleton
    fun provideAppCoroutineDispatchers(): AppDispatchers =
        AppDispatchers()

    @Provides
    @Singleton
    fun provideLogger(): Logger =
        AndroidLogcatLogger()
}