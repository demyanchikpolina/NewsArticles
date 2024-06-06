package com.demyanchikpolina.newssearch

import android.content.Context
import com.demyanchikpolina.database.NewsDatabase
import com.demyanchikpolina.news.common.AppDispatchers
import com.demyanchikpolina.news.common.Logger
import com.demyanchikpolina.news.common.androidLogcatLogger
import com.demyanchikpolina.newsapi.NewsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNewsApi(okHttpClient: OkHttpClient?): NewsApi {
        return NewsApi(
            baseUrl = BuildConfig.NEWS_API_BASE_URL,
            apiKey = BuildConfig.NEWS_API_KEY,
            okHttpClient = okHttpClient,
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
        androidLogcatLogger()
}
