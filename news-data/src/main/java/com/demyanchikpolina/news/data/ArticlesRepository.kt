package com.demyanchikpolina.news.data

import com.demyanchikpolina.database.NewsDatabase
import com.demyanchikpolina.database.models.ArticleDBO
import com.demyanchikpolina.news.data.models.Article
import com.demyanchikpolina.newsapi.NewsApi
import com.demyanchikpolina.newsapi.models.ArticleDTO
import com.demyanchikpolina.newsapi.models.ResponseDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach

class ArticlesRepository(
    private val newsApi: NewsApi,
    private val database: NewsDatabase,
) {

    fun getAll(
        mergeStrategy: MergeStrategy<RequestResult<List<Article>>> = RequestResultMergeStrategy(),
    ): Flow<RequestResult<List<Article>>> {

        val cachedAllArticles = getAllFromDatabase()
            .map { result ->
                result.map { articleDbos ->
                    articleDbos.map { it.toArticle()}
                }
            }
        val remoteArticles = getAllFromServer()
            .map { result ->
                result.map { response ->
                    response.articles.map { it.toArticle()}
                }
            }

        return remoteArticles.combine(cachedAllArticles, mergeStrategy::merge)
            .flatMapLatest { result ->
                if (result is RequestResult.Success) {
                    database.articlesDao.observeAll()
                        .map { dbos-> dbos.map { it.toArticle() } }
                        .map { RequestResult.Success(it) }
                } else {
                    flowOf(result)
                }
            }
    }

    suspend fun search(query: String? = null): Flow<Article> {
        newsApi.everything()
        TODO("Not implemented")
    }

    private fun getAllFromDatabase(): Flow<RequestResult<List<ArticleDBO>>> {
        val dbRequest = database.articlesDao::getAll
            .asFlow()
            .map { RequestResult.Success(it) }

        val start = flowOf<RequestResult<List<ArticleDBO>>>(RequestResult.InProgress())

        return merge(start, dbRequest)
    }

    private fun getAllFromServer(): Flow<RequestResult<ResponseDTO<ArticleDTO>>> {
        val apiRequest = flow { emit(newsApi.everything()) }
            .onEach { result ->
                if (result.isSuccess) {
                    saveNetworkResponseToCache(checkNotNull(result.getOrThrow()).articles)
                }
            }
            .map { it.toRequestResult() }

        val start = flowOf<RequestResult<ResponseDTO<ArticleDTO>>>(RequestResult.InProgress())

        return merge(apiRequest, start)
    }

    private suspend fun saveNetworkResponseToCache(data: List<ArticleDTO>) {
        val dbos = data.map { articleDTO -> articleDTO.toArticleDBO() }
        database.articlesDao.insert(dbos)
    }
}