package com.demyanchikpolina.news.data

import com.demyanchikpolina.database.NewsDatabase
import com.demyanchikpolina.database.models.ArticleDBO
import com.demyanchikpolina.news.common.Logger
import com.demyanchikpolina.news.data.models.Article
import com.demyanchikpolina.newsapi.NewsApi
import com.demyanchikpolina.newsapi.models.ArticleDTO
import com.demyanchikpolina.newsapi.models.ResponseDTO
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import java.text.DateFormat
import java.time.LocalDateTime
import java.util.Calendar

class ArticlesRepository @Inject constructor(
    private val newsApi: NewsApi,
    private val database: NewsDatabase,
    private val logger: Logger
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getAll(
        query: String,
        mergeStrategy: MergeStrategy<RequestResult<List<Article>>> = RequestResultMergeStrategy(),
    ): Flow<RequestResult<List<Article>>> {

        val cachedAllArticles = getAllFromDatabase()

        val remoteArticles = getAllFromServer(query)

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

    fun fetchLatest(): Flow<RequestResult<List<Article>>> {
        TODO()
    }

    private fun getAllFromDatabase(): Flow<RequestResult<List<Article>>> {
        val dbRequest = database.articlesDao::getAll
            .asFlow()
            .map { RequestResult.Success(it) }
            .catch {
                RequestResult.Error<List<ArticleDBO>>(error = it)
                logger.e(
                    tag = LOG_TAG,
                    message = "Error getting articles from DB. Cause = $it"
                )
            }

        val start = flowOf<RequestResult<List<ArticleDBO>>>(RequestResult.InProgress())

        return merge(start, dbRequest)
            .map { result ->
                result.map { articleDbos ->
                    articleDbos.map { it.toArticle()}
                }
            }
    }

    private fun getAllFromServer(query: String): Flow<RequestResult<List<Article>>> {
        val apiRequest = flow {
            emit(newsApi.everything(query))
        }
            .onEach { result ->
                if (result.isSuccess) {
                    saveNetworkResponseToCache(result.getOrThrow().articles)
                }
            }
            .onEach { result ->
                if (result.isFailure) {
                    logger.e(
                        tag = LOG_TAG,
                        message = "Error getting data from Server. Cause = ${ result.exceptionOrNull() }"
                    )
                }
            }
            .map { it.toRequestResult() }

        val start = flowOf<RequestResult<ResponseDTO<ArticleDTO>>>(RequestResult.InProgress())

        return merge(apiRequest, start)
            .map { result ->
                result.map { response ->
                    response.articles.map { it.toArticle()}
                }
            }
    }

    private suspend fun saveNetworkResponseToCache(data: List<ArticleDTO>) {
        val dbos = data.map { articleDTO -> articleDTO.toArticleDBO() }
        database.articlesDao.insert(dbos)
    }

    private companion object {

        const val LOG_TAG = "ArticlesRepository"
    }
}